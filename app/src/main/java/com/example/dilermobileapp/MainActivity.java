package com.example.dilermobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dilermobileapp.config.AlertCreating;
import com.example.dilermobileapp.declarations.ReportLogicDeclaration;
import com.example.dilermobileapp.report.PdfReport;

public class MainActivity extends AppCompatActivity {
    private ReportLogicDeclaration reportLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.carsButton).setOnClickListener(this::openCarsActivity);
        findViewById(R.id.configsButton).setOnClickListener(this::openConfigsActivity);
        findViewById(R.id.specialsButton).setOnClickListener(this::openSpecialsActivity);
        findViewById(R.id.reportsButton).setOnClickListener(this::openReportsActivity);
    }

    private void openCarsActivity(View view){
        Intent intent = new Intent(this, CarsActivity.class);
        startActivity(intent);
    }

    private void openConfigsActivity(View view){
        Intent intent = new Intent(this, ConfigsActivity.class);
        startActivity(intent);
    }

    private void openSpecialsActivity(View view){
        Intent intent = new Intent(this, SpecialsActivity.class);
        startActivity(intent);
    }

    private void openReportsActivity(View view){
        reportLogic = new PdfReport(this);
        reportLogic.createCarConfigsReport();
        AlertCreating alert = new AlertCreating(this);
        alert.getInfoBuilder("Report created")
                .setPositiveButton("Ok",
                        (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }
}