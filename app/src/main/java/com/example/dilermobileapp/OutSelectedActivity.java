package com.example.dilermobileapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class OutSelectedActivity extends AppCompatActivity {
    ListView selectedCarsListView;
    ArrayList<String> carsList;
    ArrayAdapter<String> listAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_selected_activity);
        selectedCarsListView = findViewById(R.id.carsListView);
        carsList = getIntent().getStringArrayListExtra("selectedCarNames");
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carsList);
        selectedCarsListView.setAdapter(listAdapter);
    }

}
