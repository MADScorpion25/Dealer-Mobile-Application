package com.example.dilermobileapp.report;

import android.content.Context;

import com.example.dilermobileapp.declarations.CarLogicDeclaration;
import com.example.dilermobileapp.declarations.ConfigLogicDeclaration;
import com.example.dilermobileapp.declarations.ReportLogicDeclaration;
import com.example.dilermobileapp.logic.CarServiceLogic;
import com.example.dilermobileapp.logic.ConfigServiceLogic;
import com.example.dilermobileapp.storages.CarsStorage;
import com.example.dilermobileapp.storages.ConfigsStorage;

public class PdfReport implements ReportLogicDeclaration {

    private CarLogicDeclaration carLogic;
    private ConfigLogicDeclaration configLogic;
    private Context context;

    public PdfReport(Context context) {
        carLogic = new CarServiceLogic(new CarsStorage());
        this.context = context;
        configLogic = new ConfigServiceLogic(new ConfigsStorage());
    }
    @Override
    public void createCarConfigsReport() {

    }
}
