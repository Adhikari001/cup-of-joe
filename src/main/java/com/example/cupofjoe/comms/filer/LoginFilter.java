package com.example.cupofjoe.comms.filer;

import com.example.cupofjoe.comms.context.ContextHolderService;
import com.example.cupofjoe.comms.jwt.ClaimResponse;
import com.example.cupofjoe.comms.jwt.JWTTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class LoginFilter implements Filter {

    private final JWTTokenUtil jwtTokenUtil;
    private final ContextHolderService contextHolderService;

    @Autowired
    public LoginFilter(JWTTokenUtil jwtTokenUtil, ContextHolderService contextHolderService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.contextHolderService = contextHolderService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String requestTokenHeader = request.getHeader("Authorization");
        String url = request.getRequestURI();
        log.info("Inside JWT Filter:: remote host :: {}, remote address:: {}, request uri:: {} ", servletRequest.getRemoteHost(), servletRequest.getRemoteAddr(), url);
        if (!isByPassUrl(url)) {
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                String jwtToken = requestTokenHeader.substring(7);
                ClaimResponse claims = jwtTokenUtil.getUserInformationFromToken(jwtToken);
                log.info("Username from token:: {}",  claims.getUsername() );
                contextHolderService.setContext(claims.getUserId(), claims.getUsername(), claims.getRole(), claims.getPermission());
            } else {
                log.error("JWT Token does not begin with Bearer String");
                prepareException(response, "JWT token not valid", request.getRequestURI(),
                        HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void prepareException(HttpServletResponse response, String messageString, String requestUrl, int code,
                                  String error) throws IOException {
        ErrorMessage message = new ErrorMessage(messageString, requestUrl, code, error);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(message);
        response.setStatus(code);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }


    private boolean isByPassUrl(String url) {

        final String LOCAL_SERVER = "";

        final String REGISTER = LOCAL_SERVER + "/api/v1/user/register";
        final String VALIDATE_OTP = LOCAL_SERVER + "/api/v1/user/validate-otp";
        final String LOGIN = LOCAL_SERVER + "/api/v1/user/login";


        List<String> byPassUrl = Arrays.asList(REGISTER, VALIDATE_OTP, LOGIN);

        return byPassUrl.stream().anyMatch(url::equalsIgnoreCase);
    }
}
