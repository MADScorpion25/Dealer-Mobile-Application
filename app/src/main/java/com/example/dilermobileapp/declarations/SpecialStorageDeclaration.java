package com.example.dilermobileapp.declarations;

import com.example.dilermobileapp.models.Special;

import java.util.List;

public interface SpecialStorageDeclaration {
    List<Special> getList();
    Special getSpecialById(int id);
    void add(Special special);
    void update(Special special);
    void delete(Special special);
    void deleteAll(List<Special> specials);
}
