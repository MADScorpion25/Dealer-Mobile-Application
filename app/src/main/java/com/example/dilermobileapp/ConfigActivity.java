package com.example.dilermobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.models.enums.StorageType;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        findViewById(R.id.btnSetConfig).setOnClickListener(this::saveConfiguration);
        RadioButton fileStorage = findViewById(R.id.radioButtonFileStorage);
        RadioButton databaseStorage = findViewById(R.id.radioButtonDatabaseStorage);
        fileStorage.setOnClickListener(this::setConfig);
        databaseStorage.setOnClickListener(this::setConfig);
    }
    public void saveConfiguration(View view){
        if(AppManager.getStorageType() != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    @SuppressLint("NonConstantResourceId")
    public void setConfig(View view){
        RadioButton btn = (RadioButton) view;
        switch(btn.getId()){
            case R.id.radioButtonFileStorage:
                AppManager.setStorageType(StorageType.FILE_STORAGE);
                break;
            case R.id.radioButtonDatabaseStorage:
                AppManager.setStorageType(StorageType.DATABASE_STORAGE);
                break;
            default:
                break;
        }
    }
}