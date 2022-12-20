package com.example.dilermobileapp;

import static java.util.stream.Collectors.toList;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dilermobileapp.config.AlertCreating;
import com.example.dilermobileapp.declarations.ConfigLogicDeclaration;
import com.example.dilermobileapp.declarations.SpecialLogicDeclaration;
import com.example.dilermobileapp.logic.ConfigServiceLogic;
import com.example.dilermobileapp.logic.SpecialServiceLogic;
import com.example.dilermobileapp.validation.ValidationLogicService;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.models.Special;
import com.example.dilermobileapp.storages.ConfigsStorage;
import com.example.dilermobileapp.storages.SpecialsStorage;

import java.util.List;

public class EditConfigFragment extends Fragment {

    private EditText configName;

    private EditText power;

    private EditText price;

    private Spinner specialsSpinner;

    private int id;

    private Special selSpec;

    private ConfigLogicDeclaration configLogic;

    private SpecialLogicDeclaration specialLogic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_config, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configLogic = new ConfigServiceLogic(new ConfigsStorage());
        specialLogic = new SpecialServiceLogic(new SpecialsStorage());
        Button btnCreate = (Button) view.findViewById(R.id.createCarButton);
        btnCreate.setOnClickListener(this::createCar);
        btnCreate.setText("Create");
        configName = view.findViewById(R.id.editTextConfigName);
        power = view.findViewById(R.id.editTextNumberDecimalPower);
        price = view.findViewById(R.id.editTextNumberDecimalPrice);

        specialsSpinner = view.findViewById(R.id.spinnerSpecials);

        List<Special> specials = specialLogic.getSpecialsList();

        ArrayAdapter<String> adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, specials);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialsSpinner.setAdapter(adapter);

        Intent intent = getActivity().getIntent();
        id = intent.getIntExtra("id", 0);
        if(id > 0) {
            configName.setText(intent.getStringExtra("configurationName"));
            power.setText(Short.toString(intent.getShortExtra("power", (short)0)));
            price.setText(Integer.toString(intent.getIntExtra("price", 0)));
            int specId = intent.getIntExtra("specialId", 0);
            btnCreate.setText("Edit");
            if(specId > 0) {
                List<Special> collect = specials.stream()
                        .filter(spec -> spec.getId() == specId)
                        .collect(toList());
                selSpec = collect.get(0);
                selectSpinnerItemByValue(specialsSpinner, specials.indexOf(collect.get(0)));
            }
        }

        AdapterView.OnItemSelectedListener specialSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selSpec = (Special) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        specialsSpinner.setOnItemSelectedListener(specialSelectedListener);
    }

    private void selectSpinnerItemByValue(Spinner spnr, long value) {
        ArrayAdapter adapter = (ArrayAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if(adapter.getItemId(position) == value) {
                spnr.setSelection(position);
                return;
            }
        }
    }

    @RequiresApi(api = 33)
    public void createCar(View view){
        String name = configName.getText().toString();
        short pow = Short.parseShort(power.getText().toString());
        int prs = Integer.parseInt(price.getText().toString());

        Config config = new Config();
        config.setConfigurationName(name);
        config.setPower(pow);
        config.setPrice(prs);
        if(id > 0) {
            config.setId(id);
        }
        if(selSpec != null) {
            config.setSpecial(selSpec);
        }

        ValidationLogicService.validateConfig(config).ifPresentOrElse((message) -> {
            AlertCreating alert = new AlertCreating(getActivity());
            alert.getWarningBuilder(message)
                    .setPositiveButton("Ok",
                            (dialog, which) -> dialog.cancel())
                    .create()
                    .show();
        }, () -> {
            if(configLogic.createOrUpdateConfig(config)) {
                getActivity().onBackPressed();
            }
            else {
                AlertCreating alert = new AlertCreating(getActivity());
                alert.getWarningBuilder("Config with name " + name + " already exists")
                        .setPositiveButton("Ok",
                                (dialog, which) -> dialog.cancel())
                        .create()
                        .show();
            }
        });

    }
}
