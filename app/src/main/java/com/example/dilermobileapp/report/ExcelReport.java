package com.example.dilermobileapp.report;

import android.content.Context;
import android.widget.Toast;

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

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ExcelReport implements ReportLogicDeclaration {
    private CarLogicDeclaration carLogic;
    private ConfigLogicDeclaration configLogic;
    private Context context;
    public ExcelReport(Context context) {
        carLogic = new CarServiceLogic(new CarsStorage());
        this.context = context;
        configLogic = new ConfigServiceLogic(new ConfigsStorage());
    }

    @Override
    public void createCarConfigsReport() {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Car Configs Data");

        Map<String, Object[]> data = new TreeMap<String, Object[]>();

        Integer ind = 1;
        for(Car car : getCarsList()){
            data.put(ind.toString(), new Object[] {car.getBrandName() + " " +  car.getModelName() + " " + car.getProductionYear()});
            ind++;
            data.put(ind.toString(), new Object[] {"Config", "Power", "Price"});
            ind++;
            for(Config config : getCarConfigs(car)){
                data.put(ind.toString(), new Object[] {config.getConfigurationName(), config.getPower(), config.getPrice()});
                ind++;
            }
            data.put(ind.toString(), new Object[]{});
            ind++;
        }

        CellStyle cellstyle = workbook.createCellStyle();
        cellstyle.setBorderBottom(BorderStyle.MEDIUM);
        cellstyle.setBorderLeft(BorderStyle.MEDIUM);
        cellstyle.setBorderRight(BorderStyle.MEDIUM);
        cellstyle.setBorderTop(BorderStyle.MEDIUM);

        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;

            for (Object obj : objArr)
            {
                Cell cell = row.createCell(cellnum++);
                cell.setCellStyle(cellstyle);
                if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
                else if(obj instanceof Short)
                    cell.setCellValue((Short)obj);
            }
        }
        try
        {
            File file = new File(context.getFilesDir().getPath().toString() + "car_configs_report.xlsx");
            if(!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(context.getFilesDir().getPath() + "car_configs_report.xlsx");
            workbook.write(out);
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private List<Car> getCarsList() {
        return carLogic.getCarsList();
    }

    private List<Config> getCarConfigs(Car car) {
        return carLogic.getCarConfigs(car);
    }
}
