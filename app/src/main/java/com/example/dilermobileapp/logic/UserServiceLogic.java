package com.example.dilermobileapp.logic;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.UserLogicDeclaration;
import com.example.dilermobileapp.declarations.UsersStorageDeclaration;
import com.example.dilermobileapp.models.User;
import com.example.dilermobileapp.storages.UsersStorage;

public class UserServiceLogic implements UserLogicDeclaration {

    private UsersStorageDeclaration userStorage;

    public UserServiceLogic() {
        userStorage = new UsersStorage();
    }

    @Override
    public boolean loginUser(User user) {
        User userByLogin = userStorage.getUserByLogin(user.getLogin());

        if(userByLogin == null) {
            return false;
        }

        boolean t = AppManager.encoder.matches(user.getPassword(), userByLogin.getPassword());
        return AppManager.encoder.matches(user.getPassword(), userByLogin.getPassword());
    }

    @Override
    public boolean registerUser(User user) {
        userStorage.add(user);
        return true;
    }

    @Override
    public User getUserByLogin(String login) {
        return userStorage.getUserByLogin(login);
    }
}
