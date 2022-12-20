package com.example.dilermobileapp.logic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dilermobileapp.declarations.CarLogicDeclaration;
import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.storages.CarsCarStorage;

import java.util.List;

public class CarServiceLogic implements CarLogicDeclaration {

    private final CarsCarStorage carsStorage;

    public CarServiceLogic(CarsCarStorage carsStorage) {
        this.carsStorage = carsStorage;
    }

    @Override
    public List<Car> getCarsList() {
        return carsStorage.getList();
    }

    @Override
    public boolean existsByModelName(String model) {
        return carsStorage.existsByModelName(model);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean createOrUpdateCar(Car car) {
        if(car.getId() > 0) {
            carsStorage.update(car);
        }
        else if(existsByModelName(car.getModelName()) ) {
            return false;
        }
        else {
            carsStorage.add(car);
        }
        return true;
    }

    @Override
    public void deleteCar(Car car) {
        carsStorage.delete(car);
    }

    @Override
    public List<Config> getCarConfigs(Car car) {
        return carsStorage.getCarConfigs(car);
    }
}
