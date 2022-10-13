package com.example.dilermobileapp;

import static java.util.stream.Collectors.toList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    private ListView listView;
    private ArrayList<String> listData;
    private ArrayAdapter<String> listAdapter;
    public ArrayList<String> DATA;
    private static EditCarFragment editCarFragment;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.carListView);
        listData = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listData);

        findViewById(R.id.addCarNameButton).setOnClickListener((view) -> addCarToListButton());
        findViewById(R.id.outputSelectedButton).setOnClickListener(this::outputSelectedCars);
        findViewById(R.id.editCarButton).setOnClickListener(this::editCar);
        findViewById(R.id.removeCarButton).setOnClickListener(this::removeCar);

        listView.setAdapter(listAdapter);
    }

    public void addCarToListButton(){
        String text = ((EditText)findViewById(R.id.carNameEditText)).getText().toString();
        if(!text.equals("")){
            listData.add(text);
            listAdapter.notifyDataSetChanged();
            ((EditText)findViewById(R.id.carNameEditText)).setText("");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void outputSelectedCars(View view){
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
        if(isCheckedPositions.size() == 0)return;

        ArrayList<String> selectedCars = (ArrayList<String>) Arrays.stream(getCheckedItemsPositions())
                .mapToObj((i) -> (String) listData.get(isCheckedPositions.keyAt(i)))
                .collect(toList());

        ArrayList<String> filteredCars = (ArrayList<String>) listData.stream()
                .filter(str -> {
                    for (String s : selectedCars)
                        if (s.contains(str)) return true;
                    return false;
                }).collect(toList());
        Intent selectedCarsIntent = new Intent(this, OutSelectedActivity.class);
        selectedCarsIntent.putStringArrayListExtra("selectedCarNames", filteredCars);
        startActivity(selectedCarsIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void editCar(View view){
        editCarFragment = new EditCarFragment();
        Bundle bundle = new Bundle();
        String name = Arrays.stream(getCheckedItemsPositions())
                .mapToObj(i -> listData.get(i))
                .findFirst()
                .orElse("");
        bundle.putString("carName", name);
        editCarFragment.setArguments(bundle);
        editCarFragment.show(getFragmentManager(), "editCar");
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void removeCar(View view){
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
        AtomicInteger move = new AtomicInteger();
        Arrays.stream(getCheckedItemsPositions())
                .forEach((i) -> {
                    listData.remove(isCheckedPositions.keyAt(i) - move.get());
                    isCheckedPositions.removeAt(i);
                    move.getAndIncrement();
                });
        listAdapter.notifyDataSetChanged();
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private int[] getCheckedItemsPositions(){
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
        return IntStream.range(0, isCheckedPositions.size())
                .filter(isCheckedPositions::valueAt)
                .toArray();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        String carName = editCarFragment.editCarName;
        if (!carName.equals("")){
            SparseBooleanArray sparseBooleanArray  = listView.getCheckedItemPositions();
            for(int i = 0; i < listView.getCount(); i++)
            {
                if(sparseBooleanArray.get(i))
                {
                    listData.set(i, carName);
                }
            }
            listAdapter.notifyDataSetChanged();
        }
    }
}