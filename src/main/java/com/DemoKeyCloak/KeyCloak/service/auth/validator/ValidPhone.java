package com.DemoKeyCloak.KeyCloak.service.auth.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = ValidPhoneValidator.class)
@Target({ FIELD, METHOD, PARAMETER, TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {

    String message() default "{sygma.validation.constraints.valid.phone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
