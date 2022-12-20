package com.example.dilermobileapp.declarations;

import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;

import java.util.List;

public interface ConfigLogicDeclaration {
    List<Config> getConfigsList();
    Config getConfigById(int id);
    void createOrUpdateConfig(Config config);
    void deleteConfig(Config config);
}
