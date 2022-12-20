package com.example.dilermobileapp.declarations;

import com.example.dilermobileapp.models.User;

public interface UserLogicDeclaration {
    boolean loginUser(User user);
    boolean registerUser(User user);
    boolean existsByLogin(String login);
    User getUserByLogin(String login);
}
