package com.example.dilermobileapp.declarations;

import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;

import java.util.List;

public interface CarStorageDeclaration {
    List<Car> getList();
    void add(Car Car);
    void update(Car Car);
    void delete(Car Car);
    List<Config> getCarConfigs(Car car);
    void deleteAll(List<Car> Car);
    boolean existsByModelName(String model);
}
