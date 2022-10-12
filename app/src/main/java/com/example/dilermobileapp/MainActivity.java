package com.example.dilermobileapp;

import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> listData;
    public ArrayList<String> DATA;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.carListView);
        listData = new ArrayList<>();

        findViewById(R.id.addCarNameButton).setOnClickListener((view) -> addCarToListButton());
        findViewById(R.id.outputSelectedButton).setOnClickListener((view) -> outputSelectedCars());
        findViewById(R.id.selectAllButton).setOnClickListener((view) -> selectAllCars());
        findViewById(R.id.clearSelectionButton).setOnClickListener((view) -> clearSelection());
    }

    public void addCarToListButton(){
        String text = ((EditText)findViewById(R.id.carNameEditText)).getText().toString();
        if(!text.equals("")){
            listData.add(text);
            listView.setAdapter(
                    new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listData)
            );
            ((EditText)findViewById(R.id.carNameEditText)).setText("");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void outputSelectedCars(){
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();

        String alert = IntStream.range(0, isCheckedPositions.size())
                .filter(isCheckedPositions::valueAt)
                .mapToObj((i) -> listData.get(isCheckedPositions.keyAt(i)))
                .reduce((str1, str2) -> str1 + "\n" + str2).orElse("-");

        Toast.makeText(this, alert, Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void selectAllCars(){
        IntStream.range(0, listData.size())
                .forEach((i) -> listView.setItemChecked(i, true));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clearSelection(){
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
        IntStream.range(0, isCheckedPositions.size())
                .forEach((i) -> listView.setItemChecked(isCheckedPositions.keyAt(i), false));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        DATA = savedInstanceState.getStringArrayList("DATA");
        listData = DATA;
        listView.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listData)
        );

        boolean[] states = savedInstanceState.getBooleanArray("Checked");
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
        isCheckedPositions.clear();
        for(int i = 0; i < states.length; i++){
            isCheckedPositions.put(i, states[i]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList("DATA", listData);
        boolean[] states = new boolean[listData.size()];
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
        IntStream.range(0, isCheckedPositions.size())
                .forEach((i) -> {
                    int position = isCheckedPositions.keyAt(i);
                    states[position] = isCheckedPositions.get(position);
                });
        savedInstanceState.putBooleanArray("Checked", states);

        super.onSaveInstanceState(savedInstanceState);
    }
}