package com.example.dilermobileapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dilermobileapp.config.AlertCreating;
import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.CarLogicDeclaration;
import com.example.dilermobileapp.logic.CarServiceLogic;
import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.storages.CarsCarStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EditCarFragment extends Fragment {

    private EditText brandName;

    private EditText modelName;

    private EditText productionYear;

    private int id;

    private CarLogicDeclaration carLogic;

    private Set<Config> configs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_car, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carLogic = new CarServiceLogic(new CarsCarStorage());
        Button btnCreate = (Button) view.findViewById(R.id.createCarButton);
        btnCreate.setOnClickListener(this::createCar);
        btnCreate.setText("Create");

        brandName = view.findViewById(R.id.editTextBrandName);
        modelName = view.findViewById(R.id.editTextModelName);
        productionYear = view.findViewById(R.id.editTextProductionYear);
        view.findViewById(R.id.configsButton).setOnClickListener(this::openConfigsFragment);

        Intent intent = getActivity().getIntent();
        id = intent.getIntExtra("id", 0);


        if(id > 0) {
            brandName.setText(intent.getStringExtra("brandName"));
            modelName.setText(intent.getStringExtra("modelName"));
            productionYear.setText(String.valueOf(intent.getShortExtra("productionYear", (short)0)));
            btnCreate.setText("Edit");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void openConfigsFragment(View view){
        SetConfigsFragment setConfigsFragment = new SetConfigsFragment();
        Bundle bundle = new Bundle();

        if(id > 0) {
            bundle.putInt("carId", id);
        }
        setConfigsFragment.setArguments(bundle);
        setConfigsFragment.show(getFragmentManager(), "setConfigs");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createCar(View view){
        String brand = brandName.getText().toString();
        String model = modelName.getText().toString();
        short prodYear = Short.parseShort(productionYear.getText().toString());
        if(brand.equals("") || model.equals("")) return;

        Car car = new Car(brand, model, prodYear);
        if(id > 0) {
            car.setId(id);
        }

        List<Integer> configsId = AppManager.getInfo();
        configs = configsId.stream()
                .map(id -> {
                    Config c = new Config();
                    c.setId(id);
                    return c;
                })
                .collect(Collectors.toSet());
        car.setConfigs(configs);

        if(carLogic.createOrUpdateCar(car)) {
            getActivity().onBackPressed();
        }
        else {
            AlertCreating alert = new AlertCreating(getActivity());
            alert.getWarningBuilder("Car with model " + model + " already exists")
                    .setPositiveButton("Ok",
                            (dialog, which) -> dialog.cancel())
                    .create()
                    .show();
        }
    }
}