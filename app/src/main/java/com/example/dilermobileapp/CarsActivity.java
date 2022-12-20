package com.example.dilermobileapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.CarLogicDeclaration;
import com.example.dilermobileapp.logic.CarServiceLogic;
import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.storages.CarsStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CarsActivity extends AppCompatActivity {
    private ListView listView;
    private List<Car> listData;
    private ArrayAdapter<Car> listAdapter;

    private CarLogicDeclaration carLogic;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);
        carLogic = new CarServiceLogic(new CarsStorage());

        listData = carLogic.getCarsList();
        listView = findViewById(R.id.carsListView);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listData);
        findViewById(R.id.addCarNameButton).setOnClickListener(this::addCarToListButton);
        findViewById(R.id.editCarButton).setOnClickListener(this::editCar);
        findViewById(R.id.removeCarButton).setOnClickListener(this::removeCar);

        listView.setAdapter(listAdapter);
    }

    public void addCarToListButton(View view){
        Intent intent = new Intent(this, CarFuncActivity.class);
        intent.putExtra("fragmentClassName", EditCarFragment.class.getName());
        startActivity(intent);
        refreshList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshList();
    }

    private void refreshList(){
        listData = carLogic.getCarsList();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listData);
        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void outputSelectedCars(View view){
        String text = ((EditText)findViewById(R.id.carNameEditText)).getText().toString();
        if(text.equals("")) {
            return;
        }

        Intent selectedCarsIntent = new Intent(this, OutSelectedActivity.class);
        selectedCarsIntent.putExtra("filterName", text);
        startActivity(selectedCarsIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void editCar(View view){
        int[] checkedPos = getCheckedItemsPositions();
        if(checkedPos.length != 1) return;
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
        Car car = Arrays.stream(getCheckedItemsPositions())
                .mapToObj(i -> listData.get(isCheckedPositions.keyAt(i)))
                .findFirst()
                .orElse(null);
        if(car != null){
            Intent intent = new Intent(this, CarFuncActivity.class);
            intent.putExtra("fragmentClassName", EditCarFragment.class.getName());
            intent.putExtra("id", car.getId());
            intent.putExtra("brandName", car.getBrandName());
            intent.putExtra("modelName", car.getModelName());
            intent.putExtra("productionYear", (short)car.getProductionYear());
            startActivity(intent);
        }
        refreshList();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void removeCar(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Question")
                .setMessage("Do you want to delete entity?")
                .setIcon(R.drawable.free_icon_question_8184832)
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                })
                .setPositiveButton("Yes", (dialog, id) -> {
                    dialog.dismiss();
                });
        builder.create().show();
        int i = 0;
//        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
//        ArrayList<Car> toRemove = new ArrayList<>();
//        for(int i : getCheckedItemsPositions()){
//            Car car = listData.get(isCheckedPositions.keyAt(i));
//            toRemove.add(car);
//        }
//        listData.removeAll(toRemove);
//
//        toRemove.forEach(carLogic::deleteCar);
//
//        isCheckedPositions.clear();
//
//        refreshList();
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        listData = carLogic.getCarsList();
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
}