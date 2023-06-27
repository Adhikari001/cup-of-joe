package com.example.cupofjoe.comms.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import static com.example.cupofjoe.comms.jwt.JWTTokenClaimName.*;

@Component
public class JWTTokenUtil {
    private static final long JWT_EXPIRATION_AFTER = 1;
    private static final ChronoUnit JWT_EXPIRATION_UNIT = ChronoUnit.DAYS;

    private static final String SECRET_KEY = "4GRlPB01flTTSwfKUfDPUeIIFa0yZaXQc3MAAcR8mvxEWtqAauOeMaKxLzzDAxRekOFNRmnsA0Cv44jH0FunLxQWRoFMJ7xeQiB2HgV7BxGOnq";

    public JWTTokenUtil() {
    }

        public static void main(String[] args) {
        String jwt = createJWT("hello","saurav", new String[]{"CEO", "Developer"}, "ADMIN");
        System.out.println(jwt);
//        ClaimResponse usernameFromToken = getUsernameFromToken(jwt);
//        System.out.println("User name and permission is::");
//        System.out.println(usernameFromToken.getUsername());
//        System.out.println(usernameFromToken.getPermission());
    }

    public static String createJWT(String userID , String username, String[] permission, String role) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY),
                SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();
        return Jwts.builder()
                .claim(USERNAME.getName(), username)
                .claim(PERMISSION.getName(), permission)
                .claim(ROLE.getName(), role)
                .setId(userID)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(JWT_EXPIRATION_AFTER, JWT_EXPIRATION_UNIT)))
                .signWith(hmacKey)
                .compact();
    }

    public Jws<Claims> getClaimsFromToken(String jwtString){
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY),
                SignatureAlgorithm.HS256.getJcaName());

        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString);
    }

    public ClaimResponse getUserInformationFromToken(String jwtString) {
        Jws<Claims> claimsJws = getClaimsFromToken(jwtString);
        System.out.println(claimsJws);
        Claims body = claimsJws.getBody();
        String userId = body.getId();
        String username = body.get(USERNAME.getName(), String.class);
        ArrayList<String> permissions = body.get(PERMISSION.getName(), ArrayList.class);
        String role = body.get(ROLE.getName(), String.class);
        return new ClaimResponse(userId, username, permissions, role);
    }
}
