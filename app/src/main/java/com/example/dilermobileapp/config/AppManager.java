package com.example.dilermobileapp.config;

import com.example.dilermobileapp.models.User;
import com.example.dilermobileapp.models.enums.StorageType;
import com.example.dilermobileapp.storages.DealerCenterDBHelper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class AppManager {
    private static StorageType storageType;

    private static DealerCenterDBHelper dealerCenterDBHelper;

    private static List<Integer> info;

    private static User currentUser;

    public static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static DealerCenterDBHelper getDealerCenterDBHelper() {
        return dealerCenterDBHelper;
    }

    public static void setDealerCenterDBHelper(DealerCenterDBHelper dealerCenterDBHelper) {
        AppManager.dealerCenterDBHelper = dealerCenterDBHelper;
    }

    public static StorageType getStorageType() {
        return storageType;
    }

    public static void setStorageType(StorageType storageType) {
        AppManager.storageType = storageType;
    }

    public static List<Integer> getInfo() {
        if(info == null) info = new ArrayList<>();
        return info;
    }

    public static void setInfo(List<Integer> info) {
        AppManager.info = info;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        currentUser.setPassword("");
        AppManager.currentUser = currentUser;
    }
}
