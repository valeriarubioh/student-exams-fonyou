package com.fonyou.studentexam.payload.request;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.ZoneId;
import java.util.TimeZone;

public class TimeZoneValidator implements ConstraintValidator<ValidTimeZone, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            ZoneId.of(value);
            TimeZone.getTimeZone(value);
            return true; // The value is a valid time zone identifier
        } catch (Exception e) {
            return false; // The value is not a valid time zone identifier
        }
    }
}
