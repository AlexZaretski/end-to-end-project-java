package org.multicookers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.multicookers.utils.ConcreteMulticooker;

public class DatabaseExporter {
    private static DatabaseExporter instance;
    private static final String XLSX_FILENAME = "multicookers.xlsx";

    private DatabaseExporter() {
    }

    public static synchronized DatabaseExporter getInstance() {
        if (instance == null) {
            instance = new DatabaseExporter();
        }
        return instance;
    }

    public void exportToDatabase(List<Multicooker> multicookers) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Multicookers");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Type", "Capacity", "Wattage", "Price"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            for (Multicooker multicooker : multicookers) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(multicooker.getId());
                row.createCell(1).setCellValue(multicooker.getType()!= null ? multicooker.getType() : "Unknown");
                row.createCell(2).setCellValue(multicooker.getCapacity());
                row.createCell(3).setCellValue(multicooker.getWattage());
                row.createCell(4).setCellValue(multicooker.getPrice());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(XLSX_FILENAME)) {
                workbook.write(fos);
                System.out.println("Данные успешно экспортированы в файл " + XLSX_FILENAME);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при экспорте данных в Excel: " + e.getMessage());
        }
    }

    public List<Multicooker> importFromExcel(String filename) {
        List<Multicooker> multicookers = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filename)) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    int id = (int) row.getCell(0).getNumericCellValue();
                    String type = row.getCell(1).getStringCellValue();
                    int capacity = (int) row.getCell(2).getNumericCellValue();
                    int wattage = (int) row.getCell(3).getNumericCellValue();
                    double price = row.getCell(4).getNumericCellValue();
                    multicookers.add(new ConcreteMulticooker(id, type, capacity, wattage, price));
                }
            }
            System.out.println("Данные успешно импортированы из файла " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка при импорте данных из Excel: " + e.getMessage());
        }
        return multicookers;
    }
}
