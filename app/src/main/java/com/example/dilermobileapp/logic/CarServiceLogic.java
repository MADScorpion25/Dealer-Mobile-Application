package com.example.dilermobileapp.logic;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dilermobileapp.declarations.CarLogicDeclaration;
import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.storages.CarsStorage;

import java.util.List;

public class CarServiceLogic implements CarLogicDeclaration {

    private final CarsStorage carsStorage;

    public CarServiceLogic(CarsStorage carsStorage) {
        this.carsStorage = carsStorage;
    }

    @Override
    public List<Car> getCarsList() {
        return carsStorage.getList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void createOrUpdateCar(Car car) {
        if(car.getId() > 0) {
            carsStorage.update(car);
        }
        else {
            carsStorage.add(car);
        }
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
