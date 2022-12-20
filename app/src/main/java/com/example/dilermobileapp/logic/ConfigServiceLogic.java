package com.example.dilermobileapp.logic;

import com.example.dilermobileapp.declarations.ConfigLogicDeclaration;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.storages.ConfigsStorage;

import java.util.List;

public class ConfigServiceLogic implements ConfigLogicDeclaration {
    private final ConfigsStorage configsStorage;

    public ConfigServiceLogic(ConfigsStorage configsStorage) {
        this.configsStorage = configsStorage;
    }

    @Override
    public List<Config> getConfigsList() {
        return configsStorage.getList();
    }

    @Override
    public boolean existsByConfigName(String name) {
        return configsStorage.existsByConfigName(name);
    }

    @Override
    public Config getConfigById(int id) {
        return configsStorage.getConfigById(id);
    }

    @Override
    public boolean createOrUpdateConfig(Config config) {
        if(config.getId() > 0) {
            configsStorage.update(config);
        }
        else if(existsByConfigName(config.getConfigurationName())) {
            return false;
        }
        else {
            configsStorage.add(config);
        }

        return true;
    }

    @Override
    public void deleteConfig(Config config) {
        configsStorage.delete(config);
    }
}
