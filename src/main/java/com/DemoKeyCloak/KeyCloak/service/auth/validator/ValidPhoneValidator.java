package com.DemoKeyCloak.KeyCloak.service.auth.validator;

import com.DemoKeyCloak.KeyCloak.model.common.KeyclaokCode;
import com.DemoKeyCloak.KeyCloak.model.common.PhoneNumber;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidPhoneValidator implements ConstraintValidator<ValidPhone, PhoneNumber> {

    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PhoneNumber phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return true;
        } else if (validatePhoneNumber(phoneNumber)) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            throw new BadRequestException(KeyclaokCode.INVALID_PHONE__NUMBER.getValue());
        }
    }

    public static boolean validatePhoneNumber(PhoneNumber phoneNumberRequest) {
        long numberLong = Long.parseLong(phoneNumberRequest.getNumber());
        Phonenumber.PhoneNumber phoneNumber = (new Phonenumber.PhoneNumber()).setCountryCode(Integer.parseInt(phoneNumberRequest.getCode())).setNationalNumber(numberLong);
        if (phoneNumberUtil.isValidNumber(phoneNumber)) {
            phoneNumberRequest.setNumber(String.valueOf(phoneNumber.getNationalNumber()));
            return true;
        } else {
            log.error("Error parsing phone number | countryCode: {} | phoneNumber: {}", phoneNumberRequest.getCode(), phoneNumberRequest.getNumber());
            return false;
        }
    }

}
