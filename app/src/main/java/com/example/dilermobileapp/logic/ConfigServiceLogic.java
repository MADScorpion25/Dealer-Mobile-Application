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
    public Config getConfigById(int id) {
        return configsStorage.getConfigById(id);
    }

    @Override
    public void createOrUpdateConfig(Config config) {
        if(config.getId() > 0) {
            configsStorage.update(config);
        }
        else {
            configsStorage.add(config);
        }
    }

    @Override
    public void deleteConfig(Config config) {
        configsStorage.delete(config);
    }
}
