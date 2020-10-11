package com.babyplug.softwaretesting.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class PhoneNumberValidatorTest {

    private PhoneNumberValidator underTest;

    @BeforeEach
    void setup() {
        underTest = new PhoneNumberValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "0123456789, true",
            "01234567890, false",
            "1234567890, false",
            "12345678901, false",
    })
    void itShouldValidatePhoneNumber(String phone, boolean expected) {
        boolean isValid = underTest.test(phone);
        assertThat(isValid).isEqualTo(expected);
    }

}
