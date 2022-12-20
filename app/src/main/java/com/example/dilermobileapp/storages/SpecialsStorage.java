package com.example.dilermobileapp.storages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.ConfigLogicDeclaration;
import com.example.dilermobileapp.models.Special;
import com.example.dilermobileapp.models.enums.CarClass;
import com.example.dilermobileapp.models.enums.DriveType;
import com.example.dilermobileapp.declarations.SpecialStorageDeclaration;

import java.util.ArrayList;
import java.util.List;

public class SpecialsStorage implements SpecialStorageDeclaration {
    private static ArrayList<Special> Specials;
    

    public SpecialsStorage(){
        Specials = new ArrayList<>();
    }

    @Override
    public List<Special> getList() {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        List<Special> specials = new ArrayList<>();

        Cursor cursor = db.query(DealerCenterDBHelper.SPECIAL_TABLE,null,
                DealerCenterDBHelper.USER_ID + " = ?",
                new String[] {Integer.toString(AppManager.getCurrentUser().getId())},null, null, null);
        if(cursor.moveToFirst()){
            int idInd = cursor.getColumnIndex(DealerCenterDBHelper.SPECIAL_ID);
            int classInd = cursor.getColumnIndex(DealerCenterDBHelper.CAR_CLASS);
            int driveInd = cursor.getColumnIndex(DealerCenterDBHelper.DRIVE_TYPE);
            int descInt = cursor.getColumnIndex(DealerCenterDBHelper.DESCRIPTION);
            do {
                Special Special = new Special();
                Special.setId(cursor.getInt(idInd));
                Special.setCarClass(CarClass.valueOf(cursor.getString(classInd)));
                Special.setDriveType(DriveType.valueOf(cursor.getString(driveInd)));
                Special.setDescription(cursor.getString(descInt));
                specials.add(Special);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return specials;
    }

    @Override
    public Special getSpecialById(int id) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        Cursor cursor = db.query(DealerCenterDBHelper.SPECIAL_TABLE,null,
                DealerCenterDBHelper.SPECIAL_ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null);
        int idIndex = cursor.getColumnIndex(DealerCenterDBHelper.SPECIAL_ID);
        int classInd = cursor.getColumnIndex(DealerCenterDBHelper.CAR_CLASS);
        int driveInd = cursor.getColumnIndex(DealerCenterDBHelper.DRIVE_TYPE);
        int descInt = cursor.getColumnIndex(DealerCenterDBHelper.DESCRIPTION);
        Special special = new Special();
        if(cursor.moveToFirst()){
            do {
                special = new Special();
                special.setId(cursor.getInt(idIndex));
                special.setCarClass(CarClass.valueOf(cursor.getString(classInd)));
                special.setDriveType(DriveType.valueOf(cursor.getString(driveInd)));
                special.setDescription(cursor.getString(descInt));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return special;
    }

    @Override
    public void add(Special special) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DealerCenterDBHelper.CAR_CLASS, special.getCarClass().toString());
        cv.put(DealerCenterDBHelper.DRIVE_TYPE, special.getDriveType().toString());
        cv.put(DealerCenterDBHelper.DESCRIPTION, special.getDescription());
        cv.put(DealerCenterDBHelper.USER_ID, AppManager.getCurrentUser().getId());

        db.insert(DealerCenterDBHelper.SPECIAL_TABLE, null, cv);
        db.close();
    }

    @Override
    public void update(Special special) {
        if(special.getId() <= 0) {
            return;
        }
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DealerCenterDBHelper.CAR_CLASS, special.getCarClass().toString());
        cv.put(DealerCenterDBHelper.DRIVE_TYPE, special.getDriveType().toString());
        cv.put(DealerCenterDBHelper.DESCRIPTION, special.getDescription());
        cv.put(DealerCenterDBHelper.USER_ID, AppManager.getCurrentUser().getId());

        db.update(DealerCenterDBHelper.SPECIAL_TABLE, cv,
                DealerCenterDBHelper.SPECIAL_ID + " = ?",
                new String[]{Integer.toString(special.getId())});
        db.close();
    }

    @Override
    public void delete(Special special) {
        SQLiteDatabase db = AppManager.getDealerCenterDBHelper().getReadableDatabase();

        Cursor cursor = db.query(DealerCenterDBHelper.CONFIG_TABLE,null,
                DealerCenterDBHelper.SPECIAL_ID + " = ?",
                new String[]{Integer.toString(special.getId())}, null, null, null);

        if(cursor.moveToFirst()) {
            int confInd = cursor.getColumnIndex(DealerCenterDBHelper.CONFIG_ID);
            int nameId = cursor.getColumnIndex(DealerCenterDBHelper.CONFIG_NAME);
            int powId = cursor.getColumnIndex(DealerCenterDBHelper.POWER);
            int prsId = cursor.getColumnIndex(DealerCenterDBHelper.PRICE);
            do {
                ContentValues cv = new ContentValues();
                cv.put(DealerCenterDBHelper.CONFIG_ID, cursor.getInt(confInd));
                cv.put(DealerCenterDBHelper.CONFIG_NAME, cursor.getString(nameId));
                cv.put(DealerCenterDBHelper.PRICE, cursor.getInt(prsId));
                cv.put(DealerCenterDBHelper.POWER, cursor.getShort(powId));

                db.update(DealerCenterDBHelper.CONFIG_TABLE, cv,
                        DealerCenterDBHelper.CONFIG_ID + " = ?",
                        new String[]{Integer.toString(cursor.getInt(confInd))});
            } while (cursor.moveToNext());
        }

        db.delete(DealerCenterDBHelper.SPECIAL_TABLE,
                DealerCenterDBHelper.SPECIAL_ID + " = ?",
                new String[]{Integer.toString(special.getId())});
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void deleteAll(List<Special> Specials) {
        Specials.forEach(this::delete);
    }

}
