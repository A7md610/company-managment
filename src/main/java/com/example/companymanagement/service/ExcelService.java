package com.example.companymanagement.service;

import com.example.companymanagement.entity.Company;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    public List<Company> parseExcelFile(MultipartFile file) throws IOException {
        List<Company> companies = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        // Skip header row
        if (rows.hasNext()) {
            rows.next();
        }

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            Company company = new Company();
            
            // Assuming columns are: Name, City, Employee
            company.setName(getCellValueAsString(currentRow.getCell(0)));
            company.setCity(getCellValueAsString(currentRow.getCell(1)));
            company.setEmployee(getCellValueAsString(currentRow.getCell(2)));
            
            companies.add(company);
        }

        workbook.close();
        return companies;
    }

    public ByteArrayInputStream exportToExcel(List<Company> companies) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Companies");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("City");
            headerRow.createCell(2).setCellValue("Employee");

            // Create data rows
            int rowIdx = 1;
            for (Company company : companies) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(company.getName());
                row.createCell(1).setCellValue(company.getCity());
                row.createCell(2).setCellValue(company.getEmployee());
            }

            // Auto-size columns
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
} 