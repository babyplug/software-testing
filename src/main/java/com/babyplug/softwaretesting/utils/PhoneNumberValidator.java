package com.babyplug.softwaretesting.utils;

import java.util.function.Predicate;

public class PhoneNumberValidator implements Predicate<String> {

    @Override
    public boolean test(String phoneNumber) {
        return phoneNumber.startsWith("0")
                && phoneNumber.length() == 10;
    }
}
