package com.example.dilermobileapp.report;

import android.content.Context;

import com.example.dilermobileapp.config.AlertCreating;
import com.example.dilermobileapp.declarations.CarLogicDeclaration;
import com.example.dilermobileapp.declarations.ConfigLogicDeclaration;
import com.example.dilermobileapp.declarations.ReportLogicDeclaration;
import com.example.dilermobileapp.declarations.SpecialLogicDeclaration;
import com.example.dilermobileapp.logic.CarServiceLogic;
import com.example.dilermobileapp.logic.ConfigServiceLogic;
import com.example.dilermobileapp.logic.SpecialServiceLogic;
import com.example.dilermobileapp.models.Car;
import com.example.dilermobileapp.models.Config;
import com.example.dilermobileapp.models.Special;
import com.example.dilermobileapp.storages.CarsCarStorage;
import com.example.dilermobileapp.storages.ConfigsStorage;
import com.example.dilermobileapp.storages.SpecialsStorage;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class PdfReport implements ReportLogicDeclaration {

    private CarLogicDeclaration carLogic;
    private ConfigLogicDeclaration configLogic;
    private SpecialLogicDeclaration specialLogic;
    private Context context;

    public PdfReport(Context context) {
        carLogic = new CarServiceLogic(new CarsCarStorage());
        this.context = context;
        configLogic = new ConfigServiceLogic(new ConfigsStorage());
        specialLogic = new SpecialServiceLogic(new SpecialsStorage());
    }

    @Override
    public void createCarConfigsReport() {
        Thread thread = new Thread(() -> {
            try{
                File filePath = new File(context.getFilesDir().getPath(), "car_configs_report_pdf.pdf");
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdfDoc = new PdfDocument(writer);
                pdfDoc.addNewPage();
                Document document = new Document(pdfDoc);

                for(Car car : getCarsList()){
                    Paragraph paragraph = new Paragraph(car.getBrandName() + " " + car.getModelName() + " " + car.getProductionYear());
                    paragraph.setFontSize(16);
                    paragraph.setTextAlignment(TextAlignment.CENTER);
                    document.add(paragraph);

                    float[] pointColumnWidths = {150F, 150F, 150F, 150F, 150F};
                    Table table = new Table(pointColumnWidths);
                    table.addCell(new Cell().add(new Paragraph("Config")));
                    table.addCell(new Cell().add(new Paragraph("Power")));
                    table.addCell(new Cell().add(new Paragraph("Price")));
                    table.addCell(new Cell().add(new Paragraph("Car Class")));
                    table.addCell(new Cell().add(new Paragraph("Drive Type")));
                    for (Config config : getCarConfigs(car)) {
                        table.addCell(new Cell().add(new Paragraph(config.getConfigurationName())));
                        table.addCell(new Cell().add(new Paragraph(Short.toString(config.getPower()))));
                        table.addCell(new Cell().add(new Paragraph(Integer.toString(config.getPrice()))));

                        if(config.getSpecial() != null) {
                            Special special = getConfigSpecial(config);
                            table.addCell(new Cell().add(new Paragraph(special.getCarClass().toString())));
                            table.addCell(new Cell().add(new Paragraph(special.getDriveType().toString())));
                        }
                        else {
                            table.addCell(new Cell().add(new Paragraph("EMPTY_FIELD")));
                            table.addCell(new Cell().add(new Paragraph("EMPTY_FIELD")));
                        }
                    }
                    document.add(table);
                }

                document.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {

        }
    }
    private List<Car> getCarsList() {
        return carLogic.getCarsList();
    }

    private List<Config> getCarConfigs(Car car) {
        return carLogic.getCarConfigs(car);
    }

    private Special getConfigSpecial(Config config) {
        return specialLogic.getSpecialById(config.getSpecial().getId());
    }

}
