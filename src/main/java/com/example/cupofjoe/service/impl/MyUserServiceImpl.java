package com.example.cupofjoe.service.impl;

import com.example.cupofjoe.comms.context.ContextHolderService;
import com.example.cupofjoe.comms.exceptionhandler.RestException;
import com.example.cupofjoe.comms.helper.HelperUtil;
import com.example.cupofjoe.comms.jwt.JWTTokenUtil;
import com.example.cupofjoe.comms.passwordencoder.MyPasswordEncoder;
import com.example.cupofjoe.constant.enums.OtpFor;
import com.example.cupofjoe.constant.enums.RegistrationType;
import com.example.cupofjoe.dto.Message;
import com.example.cupofjoe.dto.cafe.CafeResponse;
import com.example.cupofjoe.dto.myuser.*;
import com.example.cupofjoe.constant.enums.RegistrationStatus;
import com.example.cupofjoe.entity.MyUser;
import com.example.cupofjoe.repository.MyUserRepository;
import com.example.cupofjoe.service.MyUserService;
import com.example.cupofjoe.service.validator.MyUserValidator;
import com.example.cupofjoe.service.validator.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserServiceImpl implements MyUserService {
    private static final int otpResendBlockMinutes = 5;
    private static final int otpResendLimit = 5;
    private static final int otpRetryBlockMinute = 5;
    private static final int otpRetryLimit = 5;
    private static final int passwordRetryBlockMinute = 5;
    private static final int passwordRetryLimit = 5;
    private static final int otpTimeLimitMinute = 10;
    private final MyUserRepository myUserRepository;
    private final MyPasswordEncoder passwordEncoder;
    private final MyUserValidator myUserValidator;
    private final JWTTokenUtil jwtTokenUtil;
    private final RoleValidator roleValidator;
    private final ContextHolderService contextHolderService;

    @Autowired
    public MyUserServiceImpl(MyUserRepository myUserRepository,
                             MyPasswordEncoder passwordEncoder,
                             MyUserValidator myUserValidator,
                             JWTTokenUtil jwtTokenUtil,
                             RoleValidator roleValidator,
                             ContextHolderService contextHolderService) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.myUserValidator = myUserValidator;
        this.jwtTokenUtil = jwtTokenUtil;
        this.roleValidator = roleValidator;
        this.contextHolderService = contextHolderService;
    }

    @Override
    public RegistrationResponse registrationRequest(RegistrationRequest request) {
        request.setEmail(request.getEmail().toLowerCase());
        Pair<MyUser, String> myUserOTPPair = prepareToSaveUser(request);
        MyUser myUser = myUserRepository.saveAndFlush(myUserOTPPair.getFirst());
//        sendEmailOTP(myUser, myUserOTPPair.getSecond());
        return RegistrationResponse.builder()
                .message("Otp has been sent to email successfully")
                .otp(myUserOTPPair.getSecond())
                .id(myUser.getId())
                .build();
    }

    @Override
    public ValidateOtpResponse validateOtpRequest(ValidateOtp request) {
        MyUser myUser = myUserValidator.validateMyUser(request.getUserId());
        return validateOtp(myUser, request);
    }

    @Override
    public AddInformationResponse addInformationRequest(AddInformationRequest request) {
        validatePasswordAndConformPasswordMatch(request.getPassword(), request.getConformPassword());
        MyUser myUser= myUserValidator.validateMyUser(contextHolderService.getContext().getUserId());
        if(!myUser.getUserRegistrationStatus().equalsIgnoreCase(RegistrationStatus.ADD_INFORMATION_PENDING.name())){
            throw new RestException("US011", "Already added information.");
        }
        MyUser savedUser = myUserRepository.save(prepareToAddInformation(myUser, request));
        return AddInformationResponse.builder()
                .userId(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .cafeName(savedUser.getCafeName())
                .message("Information added successfully.")
                .build();
    }

    @Override
    public LoginResponse loginRequest(LoginRequest request) {
        Optional<MyUser> optionalMyUser = myUserRepository.findByEmail(request.getEmail());
        if(optionalMyUser.isEmpty())
            throw new RestException("US010", "Can not find user from the provided email.");
        MyUser myUser = optionalMyUser.get();
        if (!passwordEncoder.getEncoder().matches(request.getPassword(), myUser.getPassword()))
            throw new RestException("US012", "Provided password is not correct.");
        final String jwt = jwtTokenUtil.createJWT(myUser.getId(), myUser.getEmail(),new String[0], "" );
        return LoginResponse.builder()
                .userId(myUser.getId())
                .firstName(myUser.getFirstName())
                .lastName(myUser.getLastName())
                .cafeName(myUser.getCafeName())
                .token(jwt)
                .message("Login successful.")
                .build();
    }

    @Override
    public Message logout() {
        return Message.builder().message("Logged out successfully.").build();
    }

    @Override
    public List<CafeResponse> getCafeList() {
        return myUserRepository.findCafeName(contextHolderService.getContext().getUserId());
    }

    private MyUser prepareToAddInformation(MyUser myUser, AddInformationRequest request) {
        myUser.setFirstName(request.getFirstName());
        myUser.setLastName(request.getLastName());
        myUser.setPassword(passwordEncoder.getEncoder().encode(request.getPassword()));
        myUser.setCafeName(request.getCafeName());
        myUser.setUserRegistrationStatus(RegistrationStatus.REGISTERED.name());
        return myUser;
    }

    private void validatePasswordAndConformPasswordMatch(String password, String conformPassword) {
        if (!password.equals(conformPassword)) {
            throw new RestException("US009", "Password and conform password does not match.");
        }
    }

    private ValidateOtpResponse validateOtp(MyUser myUser, ValidateOtp request) {
        validateUserStatus(myUser);
        if (passwordEncoder.getEncoder().matches(request.getOtp(), myUser.getOtp())) {
            MyUser savedUser = myUserRepository.save(userOtpSuccessful(myUser));
            final String jwtToken = jwtTokenUtil.createJWT(savedUser.getId(),myUser.getEmail(), new String[0], "");
            return ValidateOtpResponse.builder()
                    .token(jwtToken)
                    .userId(savedUser.getId())
                    .message("Otp has been validated successfully.")
                    .build();
        }else {
            if (myUser.getOtpFailedAttempts() >= otpRetryLimit) {
                throw new RestException("US008", "Your account has been blocked, please try after some time");
            }
            myUser.setOtpFailedAttempts(myUser.getOtpFailedAttempts() + 1);
            myUserRepository.saveAndFlush(myUser);
            sendExceptionIfLastAttempt(otpRetryLimit - myUser.getOtpFailedAttempts());
            throw new RestException("US007", "Otp is incorrect, you have " + (otpRetryLimit - myUser.getOtpFailedAttempts()) + " attempts left");
        }

    }

    private void sendExceptionIfLastAttempt(int remainingCount) {
        if (remainingCount == 0) {
//            int hour = otpRetryBlockMinute / 60;
//            int minutes = otpRetryBlockMinute - (hour * 60);
//            throw new RestException("US008", "Your account has been deactivated, you can contact help desk or wait "
//                    .concat(String.valueOf(hour))
//                    .concat(" hour(s) and ")
//                    .concat(String.valueOf(minutes))
//                    .concat(" minutes."));
            throw new RestException("US007", "Your account has been deactivated.");
        }
    }

    private MyUser userOtpSuccessful(MyUser myUser) {
        myUser.setOtp("");
        myUser.setNumberOfPasswordRetry(0);
        myUser.setNumberOfPasswordRetry(0);
        myUser.setNumberOfOtpRetry(0);
        if (myUser.getOtpFor().equals(OtpFor.REGISTRATION_EMAIL.name())) {
            myUser.setUserRegistrationStatus(RegistrationStatus.ADD_INFORMATION_PENDING.name());
            myUser.setEmailVerificationStatus(RegistrationStatus.REGISTERED.name());
        } else if (myUser.getOtpFor().equals(OtpFor.VALIDATING_EMAIL.name())) {
            myUser.setEmailVerificationStatus(RegistrationStatus.REGISTERED.name());
        }
        return myUser;
    }

    private Pair<MyUser, String> prepareToSaveUser(RegistrationRequest request) {
        MyUser myUser = findMyUserToRegister(request.getEmail());
        validateUserStatus(myUser);
        myUser.setEmail(request.getEmail());
        myUser.setUserRegistrationStatus(RegistrationStatus.OTP_PENDING.name());
        myUser.setEmailVerificationStatus(RegistrationStatus.OTP_PENDING.name());
        myUser.setDisabled(false);
        myUser.setRegistrationType(RegistrationType.EMAIL.name());
//        myUser.setRole(roleValidator.findById(1L));
        String otp = prepareToSendOtp(myUser, OtpFor.REGISTRATION_EMAIL);
        return Pair.of(myUser, otp);
    }

    private void validateUserStatus(MyUser myUser) {
        if(myUser.isDisabled()){
            throw new RestException("US006", "User has been disabled.");
        }
        //otp resend
        validateOTPResendNotExceeded(myUser);
        //otp retry
        validateOtpRetryNotExceeded(myUser);
        //password retry
        validatePasswordRetryNotExceeded(myUser);
    }

    private void validatePasswordRetryNotExceeded(MyUser myUser) {
        validateOTP(myUser.getPasswordRetryBlockedTime(), passwordRetryBlockMinute);
        if (myUser.getNumberOfPasswordRetry() >= passwordRetryLimit) {
            myUser.setNumberOfPasswordRetry(0);
            myUser.setPasswordRetryBlockedTime(HelperUtil.getLocalDateTimeOfUTC());
            myUserRepository.saveAndFlush(myUser);
//            Duration duration = Duration.ofMinutes(passwordRetryBlockMinute);
//            long hour = duration.toHours();
//            long minutes = duration.toMinutes() - hour * 60;
//            throw new RestException("US005", "You have exceeded opt resend, You can retry after " + hour + " hour(s) and " + minutes + " minute(s).");
            throw new RestException("US005", "You have exceeded opt resend, You can retry after some time.");
        }
    }
    private void validateOTPResendNotExceeded(MyUser myUser) {
        validateOTP(myUser.getOtpResendBlockedTime(), otpResendBlockMinutes);
        if (myUser.getNumberOfOtpSend() >= otpResendLimit) {
            myUser.setNumberOfOtpSend(0);
            myUser.setOtpResendBlockedTime(HelperUtil.getLocalDateTimeOfUTC());
            myUserRepository.saveAndFlush(myUser);
//            Duration duration = Duration.ofMinutes(otpResendBlockMinutes);
//            long hour = duration.toHours();
//            long minutes = duration.toMinutes() - hour * 60;
//            throw new RestException("US002", "You have exceeded opt resend, You can retry after " + hour + " hour(s) and " + minutes + " minute(s).");
            throw new RestException("US002", "You have exceeded opt resend, You can retry after some time.");
        }
    }

    private void validateOTP(LocalDateTime blockedTime, int otpResendBlockMinutes) {
        if (blockedTime != null) {
            if (blockedTime.plusMinutes(otpResendBlockMinutes).isAfter(HelperUtil.getLocalDateTimeOfUTC())) {
                Duration duration = Duration.between(HelperUtil.getLocalDateTimeOfUTC(), blockedTime.plusMinutes(otpResendBlockMinutes));
//                long hour = duration.toHours();
//                long minutes = duration.toMinutes() - hour * 60;
//                throw new RestException("US003", "You can retry after " + hour + " hour(s) and " + minutes + " minute(s).");
                throw new RestException("US003", "You can retry after some time.");

            }
        }
    }

    private void validateOtpRetryNotExceeded(MyUser myUser) {
        validateOTP(myUser.getOtpRetryBlockedTime(), otpRetryBlockMinute);
        if (myUser.getNumberOfOtpRetry() >= otpRetryLimit) {
            myUser.setNumberOfOtpRetry(0);
            myUser.setOtpRetryBlockedTime(HelperUtil.getLocalDateTimeOfUTC());
            myUserRepository.saveAndFlush(myUser);
//            Duration duration = Duration.ofMinutes(otpRetryBlockMinute);
//            long hour = duration.toHours();
//            long minutes = duration.toMinutes() - hour * 60;
//            throw new RestException("US004", "You have exceeded opt resend, You can retry after " + hour + " hour(s) and " + minutes + " minute(s).");
            throw new RestException("US004", "You have exceeded opt resend, You can retry after some time.");

        }
    }

    private MyUser findMyUserToRegister(String email) {
        Optional<MyUser> optionalMyUser = myUserRepository.findByEmail(email);
        if (optionalMyUser.isEmpty()) return new MyUser();
        MyUser myUser = optionalMyUser.get();
        if(myUser.getUserRegistrationStatus().equalsIgnoreCase(RegistrationStatus.OTP_PENDING.name())
            || myUser.getUserRegistrationStatus().equalsIgnoreCase(RegistrationStatus.ADD_INFORMATION_PENDING.name())) return myUser;
        throw new RestException("US001","User with the given email already exists.");
    }

    private String prepareToSendOtp(MyUser myUser, OtpFor otpFor) {
        String otp = HelperUtil.generateNumeric(6);
        myUser.setNumberOfOtpSend(myUser.getNumberOfOtpSend() + 1);
        myUser.setOtp(passwordEncoder.getEncoder().encode(otp));
        myUser.setOtpTimeStamp(HelperUtil.getLocalDateTimeOfUTC());
        myUser.setOtpFor(otpFor.name());
        return otp;
    }
}
