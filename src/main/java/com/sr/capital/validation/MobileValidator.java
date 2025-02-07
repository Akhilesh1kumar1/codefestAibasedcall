package com.sr.capital.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<ValidMobileNumber, String> {

    private static final String MOBILE_NUMBER_PATTERN = "^[0-9]{10}$";

    @Override
    public void initialize(ValidMobileNumber constraintAnnotation) {
        // Initialization code, if necessary
    }

    @Override
    public boolean isValid(String mobileNumber, ConstraintValidatorContext context) {
        if (mobileNumber == null || mobileNumber.isEmpty()) {
            return true;
        }
        return mobileNumber.matches(MOBILE_NUMBER_PATTERN);
    }
}
