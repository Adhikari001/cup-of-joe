package com.example.cupofjoe.comms.helper;

import com.example.cupofjoe.comms.exceptionhandler.RestException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
public class HelperUtil {
    public static LocalDate toLocalDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(date, dateTimeFormatter);
        } catch (Exception e) {
            log.error("Date format Invalid. Couldn't parse to LocalDate");
            throw new RestException("HU001", "Wrong Date Format ".concat(date));
        }
    }

    public static LocalDateTime toLocalDateTime(String stringDate) {
        if (stringDate == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(stringDate, formatter);
        } catch (Exception e) {
            log.error("Date format Invalid. Couldn't parse to LocalDateTime");
            throw new RestException("HU004", "Wrong Date Format ".concat(stringDate));
        }
    }
    public static LocalDateTime getLocalDateTimeOfUTC() {
        return ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();
    }
    public static LocalDate getLocalDateOfUTC() {
        return ZonedDateTime.now(ZoneOffset.UTC).toLocalDate();
    }

    public static String generateNumeric(int length) {
        int leftLimit = 48;
        int rightLimit = 122;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
