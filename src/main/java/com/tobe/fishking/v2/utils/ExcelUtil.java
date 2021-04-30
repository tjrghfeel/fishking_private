package com.tobe.fishking.v2.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static List<List<String>> getExcelDataToList(String excel_name) throws IOException {
        FileInputStream fis = new FileInputStream(excel_name);
        Workbook workbook = null;
        if (excel_name.toUpperCase().endsWith(".XLS")) {
            try {
                workbook = new HSSFWorkbook(fis);
            } catch (OfficeXmlFileException e) {
                FileInputStream fis2 = new FileInputStream(excel_name);
                workbook = new XSSFWorkbook(fis2);
            }
        } else {
            workbook = new XSSFWorkbook(fis);
        }
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        List<List<String>> data_list = new ArrayList<>();
        for (int rowIndex = 1; rowIndex < rows; rowIndex++) {
            Row row = sheet.getRow(rowIndex); // 각 행을 읽어온다
            if (row != null) {
                int cells = row.getPhysicalNumberOfCells();
                List<String> data = new ArrayList<>();
                for (int columnIndex = 0; columnIndex < cells; columnIndex++) {
                    Cell cell = row.getCell(columnIndex); // 셀에 담겨있는 값을 읽는다.
                    String value = "";
                    if (cell.getCellType() != null) {
                        switch (cell.getCellType()) { // 각 셀에 담겨있는 데이터의 타입을 체크하고 해당 타입에 맞게 가져온다.
                            case NUMERIC:
                                value = Integer.toString((int) cell.getNumericCellValue());
                                break;
                            case STRING:
                                value = cell.getStringCellValue() + "";
                                break;
                            case BLANK:
                                value = cell.getBooleanCellValue() + "";
                                break;
                            case ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                        }
                    }
                    data.add(value);
                }
                data_list.add(data);
            }
        }
        return data_list;
    }

    public static String getExcelFromList(List<Map<String, String>> data, String[] headers, String[] headersEn, String file_name) throws IOException {
        String newFileName = "/mnt/nfs/files/manage/" + file_name + ".xlsx";
        try (FileOutputStream fos = new FileOutputStream(newFileName);
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = null;

            // 헤더 라인 입력
            Row row = sheet.createRow(0);
            for (int idx=0; idx < headers.length; idx++) {
                Cell cell = row.createCell(idx);
                Font font = workbook.createFont();
                font.setBold(true);
                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.DOUBLE);
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setFont(font);
                cell.setCellValue(headers[idx]);
                cell.setCellStyle(style);
            }

            // 데이터 라인별 입력
            int row_idx = 1;
            for (Map<String, String> d : data) {
                row = sheet.createRow(row_idx);
                for (int idx=0; idx < headers.length; idx++) {
                    Cell cell = row.createCell(idx);
                    // 데이터 입력
                    cell.setCellValue(d.get(headersEn[idx]));
                }
                row_idx += 1;
            }
                // 컬럼별 셀 사이즈 자동으로 맞춤
//                for (int idx=0; idx < 6; idx++) {
//                    sheet.autoSizeColumn(idx);
//                }

        }

        return file_name;
    }

}
