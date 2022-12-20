package com.example.dilermobileapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dilermobileapp.config.AlertCreating;
import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.SpecialLogicDeclaration;
import com.example.dilermobileapp.logic.SpecialServiceLogic;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.models.Special;
import com.example.dilermobileapp.storages.SpecialsStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class SpecialsActivity extends AppCompatActivity {

    private ListView listView;
    private List<Special> listData;
    private ArrayAdapter<Special> listAdapter;

    private SpecialLogicDeclaration specialLogic;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specials);
        listView = findViewById(R.id.specialsListView);
        specialLogic = new SpecialServiceLogic(new SpecialsStorage());

        listData = specialLogic.getSpecialsList();
        listView = findViewById(R.id.specialsListView);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listData);
        findViewById(R.id.addSpecialNameButton).setOnClickListener(this::addSpecialToListButton);
        findViewById(R.id.editSpecialButton).setOnClickListener(this::editSpecial);
        findViewById(R.id.removeSpecialButton).setOnClickListener(this::removeSpecial);

        listView.setAdapter(listAdapter);
    }

    public void addSpecialToListButton(View view){
        Intent intent = new Intent(this, CarFuncActivity.class);
        intent.putExtra("fragmentClassName", SpecialEditFragment.class.getName());
        startActivity(intent);
        refreshList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshList();
    }

    private void refreshList(){
        listData = specialLogic.getSpecialsList();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listData);
        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void editSpecial(View view){
        int[] checkedPos = getCheckedItemsPositions();
        if(checkedPos.length != 1) return;
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
        Special special = Arrays.stream(getCheckedItemsPositions())
                .mapToObj(i -> listData.get(isCheckedPositions.keyAt(i)))
                .findFirst()
                .orElse(null);
        if(special != null){
            Intent intent = new Intent(this, CarFuncActivity.class);
            intent.putExtra("fragmentClassName", SpecialEditFragment.class.getName());
            intent.putExtra("id", special.getId());
            intent.putExtra("description", special.getDescription());
            intent.putExtra("carClass", special.getCarClass().toString());
            intent.putExtra("driveType", special.getDriveType().toString());
            startActivity(intent);
        }
        refreshList();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void removeSpecial(View view){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
                    ArrayList<Special> toRemove = new ArrayList<>();

                    Arrays.stream(getCheckedItemsPositions()).sequential()
                            .forEach((i) ->
                                    toRemove.add(listData.get(isCheckedPositions.keyAt(i))));

                    listData.removeAll(toRemove);
                    toRemove.forEach(specialLogic::deleteSpecial);
                    isCheckedPositions.clear();
                    refreshList();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
            }
        };

        AlertCreating alert = new AlertCreating(this);
        alert.getQuestionBuilder()
                .setNegativeButton("No", dialogClickListener)
                .setPositiveButton("Yes", dialogClickListener)
                .create()
                .show();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        listData = specialLogic.getSpecialsList();
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