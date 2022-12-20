package com.example.dilermobileapp.logic;

import com.example.dilermobileapp.declarations.SpecialLogicDeclaration;
import com.example.dilermobileapp.declarations.SpecialStorageDeclaration;
import com.example.dilermobileapp.models.Special;

import java.util.List;

public class SpecialServiceLogic implements SpecialLogicDeclaration {
    private SpecialStorageDeclaration specialStorage;

    public SpecialServiceLogic(SpecialStorageDeclaration specialStorage) {
        this.specialStorage = specialStorage;
    }


    @Override
    public List<Special> getSpecialsList() {
        return specialStorage.getList();
    }

    @Override
    public Special getSpecialById(int id) {
        return specialStorage.getSpecialById(id);
    }

    @Override
    public void createOrUpdateSpecial(Special special) {
        if(special.getId() > 0) {
            specialStorage.update(special);
        }
        else {
            specialStorage.add(special);
        }
    }

    @Override
    public void deleteSpecial(Special special) {
        specialStorage.delete(special);
    }
}
