package com.online.elctronic.store.validate;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageValidator.class)
public @interface ImageNameValid {

    // Error Message
    String message() default "Invalid Image Name";

    //Represent group constraint
    Class<?>[] groups() default { };

    //Additional information about annotations
    Class<? extends Payload>[] payload() default { };

}
