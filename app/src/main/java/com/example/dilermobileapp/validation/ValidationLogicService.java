package com.example.dilermobileapp.validation;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.models.Special;
import com.example.dilermobileapp.models.User;

import java.util.Optional;

public class ValidationLogicService {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<String> validateUser(User user) {
        if(!ValidationService.checkEmailCorrect(user.getEmail())) {
            return Optional.of("Incorrect email address");
        }
        if(!ValidationService.checkPasswordCorrect(user.getPassword())) {
            return Optional.of("Password length must be between 5 and 30");
        }
        if(!ValidationService.stringIsNotEmpty(user.getLogin())) {
            return Optional.of("User login cannot be empty");
        }
        return Optional.empty();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<String> validateCar(Car car) {
        if(!ValidationService.stringIsNotEmpty(car.getBrandName())) {
            return Optional.of("Car brand cannot be empty");
        }
        if(!ValidationService.stringIsNotEmpty(car.getModelName())) {
            return Optional.of("Car model cannot be empty");
        }
        if(!ValidationService.checkYearCorrect(car.getProductionYear())) {
            return Optional.of("Year must be between 1900 and 2023");
        }
        return Optional.empty();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<String> validateConfig(Config config) {
        if(!ValidationService.stringIsNotEmpty(config.getConfigurationName())) {
            return Optional.of("Config name cannot be empty");
        }
        if(!ValidationService.checkPowerCorrect(config.getPower())) {
            return Optional.of("Power must be between 0 and 700");
        }
        if(!ValidationService.checkPriceCorrect(config.getPrice())) {
            return Optional.of("Price must be between 10_000 and 10_000_000");
        }
        return Optional.empty();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<String> validateSpecial(Special special) {
        if(!ValidationService.stringIsNotEmpty(special.getDescription())) {
            return Optional.of("Description cannot be empty");
        }
        if(!ValidationService.stringIsNotEmpty(special.getCarClass().toString())) {
            return Optional.of("Car class cannot be empty");
        }
        if(!ValidationService.stringIsNotEmpty(special.getDriveType().toString())) {
            return Optional.of("Drive type cannot be empty");
        }
        return Optional.empty();
    }
}
