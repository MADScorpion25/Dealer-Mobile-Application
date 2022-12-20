package com.example.dilermobileapp.declarations;

import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;

import java.util.List;

public interface CarLogicDeclaration {
    List<Car> getCarsList();
    boolean existsByModelName(String model);
    boolean createOrUpdateCar(Car car);
    void deleteCar(Car car);
    List<Config> getCarConfigs(Car car);
}
