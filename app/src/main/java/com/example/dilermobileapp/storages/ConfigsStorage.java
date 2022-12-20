package com.example.dilermobileapp.storages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.SpecialLogicDeclaration;
import com.example.dilermobileapp.logic.SpecialServiceLogic;
import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.declarations.ConfigStorageDeclaration;
import com.example.dilermobileapp.models.Special;
import com.example.dilermobileapp.models.enums.CarClass;
import com.example.dilermobileapp.models.enums.DriveType;

import java.util.ArrayList;
import java.util.List;

public class ConfigsStorage implements ConfigStorageDeclaration {

    private static ArrayList<Config> configs;

    private SpecialLogicDeclaration specialLogic;

    public ConfigsStorage(){
        configs = new ArrayList<>();
        specialLogic = new SpecialServiceLogic(new SpecialsStorage());
    }

    @Override
    public List<Config> getList() {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        Cursor cursor = db.query(DealerCenterDBHelper.CONFIG_TABLE,null,
                DealerCenterDBHelper.USER_ID + " = ?",
                new String[] {Integer.toString(AppManager.getCurrentUser().getId())}, null, null, null);
        List<Config> configs = getFromCursor(cursor, db);

        db.close();
        return configs;
    }

    @Override
    public Config getConfigById(int id) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        Cursor cursor = db.query(DealerCenterDBHelper.CONFIG_TABLE,null,
                DealerCenterDBHelper.CONFIG_ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null);

        List<Config> fromCursor = getFromCursor(cursor, db);

        cursor.close();
        db.close();
        return fromCursor.get(0);
    }

    @Override
    public void add(Config config) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DealerCenterDBHelper.CONFIG_NAME, config.getConfigurationName());
        cv.put(DealerCenterDBHelper.POWER, config.getPower());
        cv.put(DealerCenterDBHelper.PRICE, config.getPrice());
        cv.put(DealerCenterDBHelper.USER_ID, AppManager.getCurrentUser().getId());

        if(config.getSpecial() != null){
            cv.put(DealerCenterDBHelper.SPECIAL_ID, config.getSpecial().getId());
        }

        db.insert(DealerCenterDBHelper.CONFIG_TABLE, null, cv);
        db.close();
    }

    @Override
    public void update(Config config) {
        if(config.getId() <= 0) return;
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DealerCenterDBHelper.CONFIG_NAME, config.getConfigurationName());
        cv.put(DealerCenterDBHelper.POWER, config.getPower());
        cv.put(DealerCenterDBHelper.PRICE, config.getPrice());
        cv.put(DealerCenterDBHelper.USER_ID, AppManager.getCurrentUser().getId());
        if(config.getSpecial() != null){
            cv.put(DealerCenterDBHelper.SPECIAL_ID, config.getSpecial().getId());
        }

        db.update(DealerCenterDBHelper.CONFIG_TABLE, cv,
                DealerCenterDBHelper.CONFIG_ID + " = ?",
                new String[]{Integer.toString(config.getId())});

        db.close();
    }

    @Override
    public void delete(Config config) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        db.delete(DealerCenterDBHelper.CONFIG_TABLE,
                DealerCenterDBHelper.CONFIG_ID + " = ?",
                new String[]{Integer.toString(config.getId())});

        db.delete(DealerCenterDBHelper.CONFIG_CAR_TABLE,
                DealerCenterDBHelper.CONFIG_ID + " = ?",
                new String[]{Integer.toString(config.getId())});

        db.close();
    }

    @Override
    public boolean existsByConfigName(String name) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getWritableDatabase();

        Cursor cursor = db.query(DealerCenterDBHelper.CONFIG_TABLE,null,
                DealerCenterDBHelper.CONFIG_NAME + " = ?",
                new String[] {name}, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void deleteAll(List<Config> configs) {
        configs.forEach(this::delete);
    }
    

    @Override
    public List<Config> getConfigsByCar(Car car) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        Cursor cursor = db.query(DealerCenterDBHelper.CONFIG_TABLE, null,
                DealerCenterDBHelper.CAR_ID + " = ?",
                new String[]{Integer.toString(car.getId())}, null, null, null);

        List<Config> configs = getFromCursor(cursor, db);
        return configs;
    }

    private List<Config> getFromCursor(Cursor cursor, SQLiteDatabase db){
        List<Config> configs = new ArrayList<>();
        if(cursor.moveToFirst()){
            int idInd = cursor.getColumnIndex(DealerCenterDBHelper.CONFIG_ID);
            int nameInd = cursor.getColumnIndex(DealerCenterDBHelper.CONFIG_NAME);
            int priceInd = cursor.getColumnIndex(DealerCenterDBHelper.PRICE);
            int powerInt = cursor.getColumnIndex(DealerCenterDBHelper.POWER);
            int specId = cursor.getColumnIndex(DealerCenterDBHelper.SPECIAL_ID);
            do{
                Config config = new Config();
                config.setId(cursor.getInt(idInd));
                config.setConfigurationName(cursor.getString(nameInd));
                config.setPrice(cursor.getInt(priceInd));
                config.setPower((short) cursor.getInt(powerInt));

                int specialId = cursor.getInt(specId);
                if(specialId > 0) {
                    Special special = specialLogic.getSpecialById(specialId);
                    config.setSpecial(special);
                }

                configs.add(config);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return configs;
    }
}
