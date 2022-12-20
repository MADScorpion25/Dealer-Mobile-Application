package com.example.dilermobileapp.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationService {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean checkEmailCorrect(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static boolean checkPasswordCorrect(String password){
        return password.length() > 5 && password.length() <= 30;
    }

    public static boolean checkYearCorrect(short year){
        return year >= 1900 && year < 2023;
    }

    public static boolean checkPriceCorrect(int price){
        return price >= 10_000 && price <= 10_000_000;
    }

    public static boolean checkPowerCorrect(short power){
        return power > 0 && power < 700;
    }

    public static boolean stringIsNotEmpty(String str){
        return str != null && !"".equals(str);
    }
}
