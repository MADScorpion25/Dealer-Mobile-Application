package com.example.dilermobileapp.declarations;

import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;

import java.util.List;

public interface ConfigStorageDeclaration {
    List<Config> getList();
    Config getConfigById(int id);
    void add(Config config);
    void update(Config config);
    void delete(Config config);
    void deleteAll(List<Config> configs);
    List<Config> getConfigsByCar(Car car);
}
