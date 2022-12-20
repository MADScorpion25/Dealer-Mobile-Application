package com.example.dilermobileapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.CarLogicDeclaration;
import com.example.dilermobileapp.declarations.ConfigLogicDeclaration;
import com.example.dilermobileapp.logic.ConfigServiceLogic;
import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.storages.CarsStorage;
import com.example.dilermobileapp.storages.ConfigsStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class SetConfigsFragment extends DialogFragment {

    public List<Integer> selectedIds;

    private CarsStorage carsStorage;
    private ConfigLogicDeclaration configLogicDeclaration;

    private ListView listView;
    private List<Config> listData;
    private ArrayAdapter listAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        int carId = bundle.getInt("carId");

        getDialog().setTitle("Select configs");
        View v = inflater.inflate(R.layout.fragment_set_configs, null);
        Button button = v.findViewById(R.id.addConfigButton);

        carsStorage = new CarsStorage();
        configLogicDeclaration = new ConfigServiceLogic(new ConfigsStorage());

        Car car = new Car();
        car.setId(carId);
        listData = carsStorage.getCarConfigs(car);
        listView = v.findViewById(R.id.configsListView);
        listAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.select_dialog_multichoice, listData);

        int size = listData.size();

        List<Config> configsList = configLogicDeclaration.getConfigsList();
        configsList.removeAll(listData);
        listData.addAll(configsList);

        listView.setAdapter(listAdapter);
        IntStream.range(0, size)
                        .forEach(id -> listView.setItemChecked(id, true));


        button.setOnClickListener(u -> {
            int[] checkedItemsPositions = getCheckedItemsPositions();
            selectedIds = new ArrayList<>();
            Arrays.stream(checkedItemsPositions)
                            .forEach(id ->
                                selectedIds.add(listData.get(id).getId())
                            );
            AppManager.setInfo(selectedIds);
            dismiss();
        });
        return v;
    }
    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int[] getCheckedItemsPositions(){
        SparseBooleanArray isCheckedPositions = listView.getCheckedItemPositions();
        return IntStream.range(0, isCheckedPositions.size())
                .filter(isCheckedPositions::valueAt)
                .toArray();
    }

}