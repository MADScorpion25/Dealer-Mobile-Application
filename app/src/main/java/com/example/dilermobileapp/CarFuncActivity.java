package com.example.dilermobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class CarFuncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_func);
        setSelectedFragment();
    }
    private void setSelectedFragment(){
        Intent intent = getIntent();
        FragmentTransaction frTransaction = getSupportFragmentManager().beginTransaction();
        String className = intent.getStringExtra("fragmentClassName");

        if(EditCarFragment.class.getName().equals(className)) {
            frTransaction.replace(R.id.carsActivityFrame, new EditCarFragment());
        }
        else if(EditConfigFragment.class.getName().equals(className)) {
            frTransaction.replace(R.id.carsActivityFrame, new EditConfigFragment());
        }
        else if(SpecialEditFragment.class.getName().equals(className)) {
            frTransaction.replace(R.id.carsActivityFrame, new SpecialEditFragment());
        }
        frTransaction.commit();
    }
}