package com.example.dilermobileapp.declarations;

import com.example.dilermobileapp.models.Special;

import java.util.List;

public interface SpecialLogicDeclaration {
    List<Special> getSpecialsList();
    Special getSpecialById(int id);
    void createOrUpdateSpecial(Special special);
    void deleteSpecial(Special special);
}
