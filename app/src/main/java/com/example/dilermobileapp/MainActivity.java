package com.example.dilermobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.ReportLogicDeclaration;
import com.example.dilermobileapp.report.ExcelReport;
import com.example.dilermobileapp.report.PdfReport;
import com.example.dilermobileapp.report.WordReport;
import com.example.dilermobileapp.storages.DealerCenterDBHelper;

public class MainActivity extends AppCompatActivity {
    private ReportLogicDeclaration reportLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reportLogic = new ExcelReport(this);
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
        reportLogic.createCarConfigsReport();
        reportLogic = new WordReport(this);
        reportLogic.createCarConfigsReport();
        reportLogic = new PdfReport(this);
        reportLogic.createCarConfigsReport();
    }
}