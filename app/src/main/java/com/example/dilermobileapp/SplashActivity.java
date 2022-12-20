package com.example.dilermobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.storages.DealerCenterDBHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.setDealerCenterDBHelper(new DealerCenterDBHelper(this));
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}