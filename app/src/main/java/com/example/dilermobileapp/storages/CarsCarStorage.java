package com.example.dilermobileapp.storages;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.ConfigLogicDeclaration;
import com.example.dilermobileapp.logic.ConfigServiceLogic;
import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.declarations.CarStorageDeclaration;
import com.example.dilermobileapp.models.Config;

import java.util.ArrayList;
import java.util.List;

public class CarsCarStorage implements CarStorageDeclaration {

    private static ArrayList<Car> cars;

    private ConfigLogicDeclaration configLogic;

    public CarsCarStorage(){
        cars = new ArrayList<>();
        configLogic = new ConfigServiceLogic(new ConfigsStorage());
    }

    @Override
    public List<Car> getList() {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getWritableDatabase();

        List<Car> cars = new ArrayList<>();

        Cursor cursor = db.query(DealerCenterDBHelper.CAR_TABLE,null,
                DealerCenterDBHelper.USER_ID + " = ?",
                new String[] {Integer.toString(AppManager.getCurrentUser().getId())}, null, null, null);
        if(cursor.moveToFirst()){
            int idInd = cursor.getColumnIndex(DealerCenterDBHelper.CAR_ID);
            int brandInd = cursor.getColumnIndex(DealerCenterDBHelper.BRAND_NAME);
            int modelInd = cursor.getColumnIndex(DealerCenterDBHelper.MODEL_NAME);
            int prodInt = cursor.getColumnIndex(DealerCenterDBHelper.PRODUCTION_YEAR);
            do {
                Car car = new Car();
                car.setId(cursor.getInt(idInd));
                car.setBrandName(cursor.getString(brandInd));
                car.setModelName(cursor.getString(modelInd));
                car.setProductionYear((short) cursor.getInt(prodInt));
                cars.add(car);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cars;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void add(Car car) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DealerCenterDBHelper.MODEL_NAME, car.getModelName());
        cv.put(DealerCenterDBHelper.BRAND_NAME, car.getBrandName());
        cv.put(DealerCenterDBHelper.PRODUCTION_YEAR, car.getProductionYear());
        cv.put(DealerCenterDBHelper.USER_ID, AppManager.getCurrentUser().getId());
        long insert = db.insert(DealerCenterDBHelper.CAR_TABLE, null, cv);

        if(car.getConfigs() != null) {
            car.getConfigs()
                    .forEach(config -> {
                        ContentValues conV = new ContentValues();
                        conV.put(DealerCenterDBHelper.CAR_ID, insert);
                        conV.put(DealerCenterDBHelper.CONFIG_ID, config.getId());
                        db.insert(DealerCenterDBHelper.CONFIG_CAR_TABLE,
                                null, conV);
                    });
        }

        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(Car car) {
        if(car.getId() <= 0) {
            return;
        }
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DealerCenterDBHelper.MODEL_NAME, car.getModelName());
        cv.put(DealerCenterDBHelper.BRAND_NAME, car.getBrandName());
        cv.put(DealerCenterDBHelper.PRODUCTION_YEAR, car.getProductionYear());
        cv.put(DealerCenterDBHelper.USER_ID, AppManager.getCurrentUser().getId());

        db.update(DealerCenterDBHelper.CAR_TABLE, cv,
                DealerCenterDBHelper.CAR_ID + " = ?",
                new String[]{Integer.toString(car.getId())});


        db.delete(DealerCenterDBHelper.CONFIG_CAR_TABLE,
                DealerCenterDBHelper.CAR_ID + " = ?",
                new String[]{Integer.toString(car.getId())});

        setCarConfigs(car, db);

        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setCarConfigs(Car car, SQLiteDatabase db){
        if(car.getConfigs() == null) {
            return;
        }
        car.getConfigs().forEach(conf -> {
            ContentValues cov = new ContentValues();
            cov.put(DealerCenterDBHelper.CAR_ID, car.getId());
            cov.put(DealerCenterDBHelper.CONFIG_ID, conf.getId());
            db.insert(DealerCenterDBHelper.CONFIG_CAR_TABLE, null, cov);
        });
    }

    @Override
    public void delete(Car car) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        db.delete(DealerCenterDBHelper.CAR_TABLE,
                DealerCenterDBHelper.CAR_ID + " = ?",
                new String[]{Integer.toString(car.getId())});

        db.delete(DealerCenterDBHelper.CONFIG_CAR_TABLE,
                DealerCenterDBHelper.CAR_ID + " = ?",
                new String[]{Integer.toString(car.getId())});

        db.close();
    }

    @Override
    public List<Config> getCarConfigs(Car car) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        List<Config> configs = new ArrayList<>();

        Cursor cursor = db.query(DealerCenterDBHelper.CONFIG_CAR_TABLE,
                new String[]{DealerCenterDBHelper.CONFIG_ID},
                DealerCenterDBHelper.CAR_ID + " = ?",
                new String[]{Integer.toString(car.getId())}, null, null, null);

        if(cursor.moveToFirst()) {
            int idInd = cursor.getColumnIndex(DealerCenterDBHelper.CONFIG_ID);
            do {
                Config configById = configLogic.getConfigById(cursor.getInt(idInd));
                configs.add(configById);
            } while(cursor.moveToNext());
        }

        return configs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void deleteAll(List<Car> cars) {
        cars.forEach(this::delete);
    }

    @Override
    public boolean existsByModelName(String model) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getWritableDatabase();

        Cursor cursor = db.query(DealerCenterDBHelper.CAR_TABLE,null,
                DealerCenterDBHelper.MODEL_NAME + " = ?",
                new String[] {model}, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }
}
