package com.errorfreetext.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CorrectionTextValidator implements ConstraintValidator<ValidCorrectionText, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.length() < 3) {
            return reject(context, "Text must contain at least 3 characters");
        }

        if (value.chars().allMatch(ch -> !Character.isLetter(ch))) {
            return reject(context, "Text cannot contain only special characters and digits");
        }

        return true;
    }

    private boolean reject(ConstraintValidatorContext context, String message) {
        if (context != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return false;
    }
}
