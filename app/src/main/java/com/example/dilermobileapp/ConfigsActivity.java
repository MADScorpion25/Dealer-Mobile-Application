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
import android.widget.RemoteViews;

import com.example.dilermobileapp.config.AlertCreating;
import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.ConfigLogicDeclaration;
import com.example.dilermobileapp.logic.ConfigServiceLogic;
import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.storages.ConfigsStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ConfigsActivity extends AppCompatActivity {

    private ListView listView;
    private List<Config> listData;
    private ArrayAdapter<Config> listAdapter;

    private ConfigLogicDeclaration configLogic;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configs);
        listView = findViewById(R.id.configsListView);
        configLogic = new ConfigServiceLogic(new ConfigsStorage());

        listData = configLogic.getConfigsList();
        listView = findViewById(R.id.configsListView);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listData);
        findViewById(R.id.addConfigNameButton).setOnClickListener(this::addConfigToListButton);
        findViewById(R.id.editConfigButton).setOnClickListener(this::editConfig);
        findViewById(R.id.removeConfigButton).setOnClickListener(this::removeConfig);

        listView.setAdapter(listAdapter);
    }

    public void addConfigToListButton(View view){
        Intent intent = new Intent(this, CarFuncActivity.class);
        intent.putExtra("fragmentClassName", EditConfigFragment.class.getName());
        startActivity(intent);
        refreshList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshList();
    }

    private void refreshList(){
        listData = configLogic.getConfigsList();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_multichoice, listData);
        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void editConfig(View view){
        int[] checkedPos = getCheckedItemsPositions();
        if(checkedPos.length != 1) return;
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
        Config config = Arrays.stream(getCheckedItemsPositions())
                .mapToObj(i -> listData.get(isCheckedPositions.keyAt(i)))
                .findFirst()
                .orElse(null);

        if(config != null){
            Intent intent = new Intent(this, CarFuncActivity.class);
            intent.putExtra("fragmentClassName", EditConfigFragment.class.getName());
            intent.putExtra("id", config.getId());
            intent.putExtra("configurationName", config.getConfigurationName());
            intent.putExtra("price", config.getPrice());
            intent.putExtra("power", (short)config.getPower());
            intent.putExtra("specialId", config.getSpecial().getId());
            startActivity(intent);
        }
        refreshList();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void removeConfig(View view){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
                    ArrayList<Config> toRemove = new ArrayList<>();

                    Arrays.stream(getCheckedItemsPositions()).sequential()
                            .forEach((i) ->
                                    toRemove.add(listData.get(isCheckedPositions.keyAt(i))));
                    listData.removeAll(toRemove);
                    toRemove.forEach(configLogic::deleteConfig);
                    isCheckedPositions.clear();
                    refreshList();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
            }
        };

        AlertCreating alert = new AlertCreating(this);
        alert.onCreateDialog()
                .setNegativeButton("No", dialogClickListener)
                .setPositiveButton("Yes", dialogClickListener)
                .create()
                .show();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        listData = configLogic.getConfigsList();
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