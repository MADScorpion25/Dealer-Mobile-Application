package com.example.dilermobileapp.storages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.models.User;
import com.example.dilermobileapp.declarations.UsersStorageDeclaration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UsersStorage implements UsersStorageDeclaration {

    private static ArrayList<User> users;

    public UsersStorage(){
        users = new ArrayList<>();
    }

    @Override
    public User getUserById(int id) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        Cursor cursor = db.query(DealerCenterDBHelper.USER_TABLE,null,
                DealerCenterDBHelper.USER_ID + " = ?",
                new String[] {Integer.toString(id)}, null, null, null);
        if(cursor.moveToFirst()){
            int idInd = cursor.getColumnIndex(DealerCenterDBHelper.USER_ID);
            int loginInd = cursor.getColumnIndex(DealerCenterDBHelper.LOGIN);
            int passInd = cursor.getColumnIndex(DealerCenterDBHelper.PASSWORD);
            int emailInd = cursor.getColumnIndex(DealerCenterDBHelper.EMAIL);
            User user = new User();
            user.setId(cursor.getInt(idInd));
            user.setLogin(cursor.getString(loginInd));
            user.setEmail(cursor.getString(emailInd));
            user.setPassword(cursor.getString(passInd));
            cursor.close();
            db.close();
            return user;
        }
        return null;
    }

    @Override
    public User getUserByLogin(String login) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        Cursor cursor = db.query(DealerCenterDBHelper.USER_TABLE,null,
                DealerCenterDBHelper.LOGIN + " = ?",
                new String[] {login}, null, null, null);
        if(cursor.moveToFirst()){
            int idInd = cursor.getColumnIndex(DealerCenterDBHelper.USER_ID);
            int loginInd = cursor.getColumnIndex(DealerCenterDBHelper.LOGIN);
            int passInd = cursor.getColumnIndex(DealerCenterDBHelper.PASSWORD);
            int emailInd = cursor.getColumnIndex(DealerCenterDBHelper.EMAIL);
            User user = new User();
            user.setId(cursor.getInt(idInd));
            user.setLogin(cursor.getString(loginInd));
            user.setEmail(cursor.getString(emailInd));
            user.setPassword(cursor.getString(passInd));
            cursor.close();
            db.close();
            return user;
        }
        return null;
    }

    @Override
    public void add(User user) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DealerCenterDBHelper.LOGIN, user.getLogin());
        cv.put(DealerCenterDBHelper.EMAIL, user.getEmail());
        cv.put(DealerCenterDBHelper.PASSWORD,
                AppManager.encoder.encode(
                        user.getPassword()
                ));

        db.insert(DealerCenterDBHelper.USER_TABLE, null, cv);
        db.close();
    }

    @Override
    public void update(User user) {
        if(user.getId() <= 0) {
            return;
        }
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DealerCenterDBHelper.LOGIN, user.getLogin());
        cv.put(DealerCenterDBHelper.EMAIL, user.getEmail());
        cv.put(DealerCenterDBHelper.PASSWORD,
                AppManager.encoder.encode(
                        user.getPassword()
                ));

        db.update(DealerCenterDBHelper.USER_TABLE, cv,
                DealerCenterDBHelper.USER_ID + " = ?",
                new String[]{Integer.toString(user.getId())});
        db.close();
    }

    @Override
    public void delete(User user) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        db.delete(DealerCenterDBHelper.USER_TABLE,
                DealerCenterDBHelper.USER_ID + " = ?",
                new String[]{Integer.toString(user.getId())});
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void deleteAll(List<User> users) {
        users.forEach(this::delete);
    }
}
