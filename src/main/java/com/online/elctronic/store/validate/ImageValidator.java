package com.online.elctronic.store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageValidator implements ConstraintValidator<ImageNameValid, String> {

    private Logger logger = LoggerFactory.getLogger(ImageValidator.class);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        logger.info("Message is invalid {}", value);

        if(value.isBlank()){
            return false;
        } else {
            return true;
        }

    }
}
