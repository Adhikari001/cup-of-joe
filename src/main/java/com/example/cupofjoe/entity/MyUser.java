package com.example.cupofjoe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class MyUser {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

    @Id
    @Column(name = "id", nullable = false)
    private final String id = UUID.randomUUID().toString();

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "cafe_name")
    private String cafeName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_for")
    private String otpFor;

    @Column(name = "otp_time_stamp")
    private LocalDateTime otpTimeStamp;

    @Column(name = "number_otp_send")
    private int numberOfOtpSend;

    @Column(name = "otp_resend_blocked_time")
    private LocalDateTime otpResendBlockedTime;

    @Column(name = "number_of_otp_retry")
    private int numberOfOtpRetry;

    @Column(name = "otp_retry_blocked_time")
    private LocalDateTime otpRetryBlockedTime;

    @Column(name = "otp_failed_attempts")
    private int otpFailedAttempts;

    @Column(name = "number_of_password_retry")
    private int numberOfPasswordRetry;

    @Column(name = "password_resend_blocked_time")
    private LocalDateTime passwordRetryBlockedTime;

    @Column(name = "registration_type")
    private String registrationType;

    @Column(name = "user_registration_status")
    private String userRegistrationStatus;


    @Column(name = "email_verification_status")
    private String emailVerificationStatus;

    @Column(name = "is_disabled")
    private boolean isDisabled;

//    @OneToOne(fetch = FetchType.EAGER)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JoinColumn(name = "role", referencedColumnName = "id")
//    private Role role;
}
