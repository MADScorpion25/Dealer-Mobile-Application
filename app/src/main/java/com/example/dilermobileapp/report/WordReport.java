package com.example.dilermobileapp.report;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dilermobileapp.config.AppManager;
import com.example.dilermobileapp.declarations.CarLogicDeclaration;
import com.example.dilermobileapp.declarations.ConfigLogicDeclaration;
import com.example.dilermobileapp.declarations.ReportLogicDeclaration;
import com.example.dilermobileapp.logic.CarServiceLogic;
import com.example.dilermobileapp.logic.ConfigServiceLogic;
import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.storages.CarsStorage;
import com.example.dilermobileapp.storages.ConfigsStorage;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WordReport implements ReportLogicDeclaration {
    private CarLogicDeclaration carLogic;
    private ConfigLogicDeclaration configLogic;
    private Context context;

    public WordReport(Context context) {
        carLogic = new CarServiceLogic(new CarsStorage());
        this.context = context;
        configLogic = new ConfigServiceLogic(new ConfigsStorage());
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void createCarConfigsReport() {
        XWPFDocument document = new XWPFDocument();

        for(Car car : getCarsList()){
            createParagraph(document, car.getBrandName() + " " + car.getModelName() + " " + car.getProductionYear());
            createTable(document, getCarConfigs(car));
            createParagraph(document, "");
        }

        try
        {
            File file = new File(context.getFilesDir().getPath() + "car_configs_word_report.docx");
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(context.getFilesDir().getPath() + "car_configs_word_report.docx");
            document.write(out);
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void createParagraph(XWPFDocument doc, String par){
        XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontSize(12);
        run.setFontFamily("Times New Roman");
        run.setText(par);
        run.addBreak();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createTable(XWPFDocument doc, List<Config> configs){
        XWPFTable table = doc.createTable();
        table.removeRow(0);
        XWPFTableRow row = table.createRow();

        row.addNewTableCell().setText("Config");
        row.addNewTableCell().setText("Power");
        row.addNewTableCell().setText("Price");

        configs.forEach(conf -> {
            XWPFTableRow rowInfo = table.createRow();
            rowInfo.getCell(0).setText(conf.getConfigurationName());
            rowInfo.getCell(1).setText(Integer.toString(conf.getPower()));
            rowInfo.getCell(2).setText(Integer.toString(conf.getPrice()));
        });

    }

    private List<Car> getCarsList() {
        return carLogic.getCarsList();
    }

    private List<Config> getCarConfigs(Car car) {
        return carLogic.getCarConfigs(car);
    }
}
