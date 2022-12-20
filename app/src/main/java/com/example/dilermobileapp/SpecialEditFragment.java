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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.SpecialLogicDeclaration;
import com.example.dilermobileapp.logic.CarServiceLogic;
import com.example.dilermobileapp.logic.SpecialServiceLogic;
import com.example.dilermobileapp.models.Special;
import com.example.dilermobileapp.models.enums.CarClass;
import com.example.dilermobileapp.models.enums.DriveType;
import com.example.dilermobileapp.storages.CarsStorage;
import com.example.dilermobileapp.storages.SpecialsStorage;

import java.util.List;
import java.util.stream.Stream;

public class SpecialEditFragment extends Fragment {

    private Spinner carClass;

    private Spinner driveType;

    private EditText description;

    private int id;

    private String carCls;

    private String drive;

    private SpecialLogicDeclaration specialLogic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_special_edit, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        specialLogic = new SpecialServiceLogic(new SpecialsStorage());
        Button btnCreate = (Button) view.findViewById(R.id.createCarButton);
        btnCreate.setOnClickListener(this::createSpecial);
        btnCreate.setText("Create");

        carClass = view.findViewById(R.id.spinnerCarClass);
        driveType = view.findViewById(R.id.spinnerDriveType);
        description = view.findViewById(R.id.editTextMultiLineDesc);

        List<String> carClasses = Stream.of(CarClass.values())
                .map(CarClass::toString).collect(toList());
        List<String> driveTypes = Stream.of(DriveType.values())
                .map(DriveType::toString).collect(toList());

        Intent intent = getActivity().getIntent();
        id = intent.getIntExtra("id", 0);


        ArrayAdapter<String> adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, carClasses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carClass.setAdapter(adapter);

        adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, driveTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driveType.setAdapter(adapter);

        if(id > 0) {
            description.setText(intent.getStringExtra("description"));
            btnCreate.setText("Edit");
            carCls = intent.getStringExtra("carClass");
            selectSpinnerItemByValue(carClass, carClasses.indexOf(carCls));
            drive = intent.getStringExtra("driveType");
            selectSpinnerItemByValue(driveType, driveTypes.indexOf(drive));
        }

        AdapterView.OnItemSelectedListener carClassSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carCls = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(id > 0) {
                    carCls = intent.getStringExtra("carClass");
                    parent.setSelection(carClasses.indexOf(carCls));
                }
            }
        };
        carClass.setOnItemSelectedListener(carClassSelectedListener);

        AdapterView.OnItemSelectedListener driveTypeSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drive = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(id > 0) {
                    drive = intent.getStringExtra("driveType");
                    parent.setSelection(driveTypes.indexOf(drive));
                }
            }
        };
        driveType.setOnItemSelectedListener(driveTypeSelectedListener);

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

    public void createSpecial(View view){
        String desc = description.getText().toString();

        if(desc.equals("") || carCls == null || drive == null) return;

        Special special = new Special();
        special.setDescription(desc);
        special.setDriveType(DriveType.valueOf(drive));
        special.setCarClass(CarClass.valueOf(carCls));
        if(id > 0) {
            special.setId(id);
        }
        specialLogic.createOrUpdateSpecial(special);
        getActivity().onBackPressed();
    }
}