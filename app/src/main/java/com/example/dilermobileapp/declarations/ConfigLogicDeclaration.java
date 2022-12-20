package com.example.dilermobileapp.declarations;

import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;

import java.util.List;

public interface ConfigLogicDeclaration {
    List<Config> getConfigsList();
    boolean existsByConfigName(String name);
    Config getConfigById(int id);
    boolean createOrUpdateConfig(Config config);
    void deleteConfig(Config config);
}
