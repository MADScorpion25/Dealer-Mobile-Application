package com.example.dilermobileapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.models.Car;

import java.util.ArrayList;

public class OutSelectedActivity extends AppCompatActivity {
    ListView selectedCarsListView;
    ArrayList<Car> carsList;
    ArrayAdapter<Car> listAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_selected_activity);
        selectedCarsListView = findViewById(R.id.carsListView);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carsList);
        selectedCarsListView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

}
