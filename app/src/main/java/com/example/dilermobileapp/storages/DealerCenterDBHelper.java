package com.example.dilermobileapp.storages;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DealerCenterDBHelper extends SQLiteOpenHelper {

    public static final String CAR_TABLE = "cars";
    public static final String CONFIG_TABLE = "configs";
    public static final String USER_TABLE = "users";
    public static final String SPECIAL_TABLE = "specials";
    public static final String CONFIG_CAR_TABLE = "configs_cars";

    public static final String CAR_ID = "car_id";
    public static final String BRAND_NAME = "brand_name";
    public static final String MODEL_NAME = "model_name";
    public static final String PRODUCTION_YEAR = "production_year";

    public static final String CONFIG_CAR_ID = "config_car_id";

    public static final String CONFIG_ID = "config_id";
    public static final String CONFIG_NAME = "config_name";
    public static final String PRICE = "price";
    public static final String POWER = "power";

    public static final String SPECIAL_ID = "special_id";
    public static final String CAR_CLASS = "car_class";
    public static final String DRIVE_TYPE = "drive_type";
    public static final String DESCRIPTION = "description";

    public static final String USER_ID = "user_id";
    public static final String LOGIN = "login";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    public DealerCenterDBHelper(Context context){
        super(context, "dealer_app.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CAR_TABLE + " ("
                + CAR_ID + " integer primary key autoincrement, "
                + BRAND_NAME + " text not null, "
                + MODEL_NAME + " text unique not null, "
                + PRODUCTION_YEAR + " integer not null, "
                + USER_ID + " integer not null"
                + ");"
                );

        sqLiteDatabase.execSQL("create table " + CONFIG_TABLE + " ("
                + CONFIG_ID + " integer primary key autoincrement, "
                + CONFIG_NAME + " text unique not null, "
                + POWER + " integer not null, "
                + PRICE + " integer not null, "
                + SPECIAL_ID + " integer, "
                + USER_ID + " integer not null"
                + ");"
        );

        sqLiteDatabase.execSQL("create table " + CONFIG_CAR_TABLE + " ("
                + CONFIG_CAR_ID + " integer primary key autoincrement, "
                + CAR_ID + " integer not null, "
                + CONFIG_ID + " integer not null "
                + ");"
        );

        sqLiteDatabase.execSQL("create table " + SPECIAL_TABLE + " ("
                + SPECIAL_ID + " integer primary key autoincrement, "
                + CAR_CLASS + " text not null, "
                + DRIVE_TYPE + " text not null, "
                + DESCRIPTION + " text not null, "
                + USER_ID + " integer not null"
                + ");"
        );

        sqLiteDatabase.execSQL("create table " + USER_TABLE + " ("
                + USER_ID + " integer primary key autoincrement, "
                + LOGIN + " text unique not null, "
                + EMAIL + " text unique not null, "
                + PASSWORD + " text not null"
                + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + CAR_TABLE + ";");
        sqLiteDatabase.execSQL("drop table if exists " + CONFIG_TABLE + ";");
        sqLiteDatabase.execSQL("drop table if exists " + USER_TABLE + ";");
        sqLiteDatabase.execSQL("drop table if exists " + CONFIG_CAR_TABLE + ";");
    }
}
