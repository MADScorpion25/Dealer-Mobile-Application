package com.example.dilermobileapp.declarations;

import com.example.dilermobileapp.models.User;

import java.util.List;

public interface UsersStorageDeclaration {
    User getUserById(int id);
    User getUserByLogin(String login);
    void add(User user);
    void update(User user);
    void delete(User user);
    boolean existsByLogin(String login);
    void deleteAll(List<User> users);
}
