package academits.ru.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("Шестяков", "Владимир", 32, "+79323344343"));
        persons.add(new Person("Хаметова", "Милана", 23, "+79323344332"));
        persons.add(new Person("Непомнящий", "Роман", 19, "+79323345723"));
        persons.add(new Person("Шнуров", "Алексей", 25, "+79323349865"));
        persons.add(new Person("Морозов", "Игорь", 40, "+79323342345"));

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream("contacts.xlsx")) {
            Sheet sheet = workbook.createSheet("Контакты");

            XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
            XSSFFont headerFont = (XSSFFont) workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);

            headerStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 47, (byte) 84, (byte) 150}, null));
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(headerStyle);

            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setAlignment(HorizontalAlignment.CENTER);
            bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(bodyStyle);

            XSSFCellStyle evenRowsCellStyle = (XSSFCellStyle) workbook.createCellStyle();
            evenRowsCellStyle.cloneStyleFrom(bodyStyle);
            evenRowsCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 220, (byte) 230, (byte) 241}, null));
            evenRowsCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] headerCells = {"Фамилия", "Имя", "Возраст", "Телефон"};
            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(22);

            for (int i = 0; i < headerCells.length; ++i) {
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(headerCells[i]);
                headerCell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < persons.size(); ++i) {
                Row bodyRow = sheet.createRow(i + 1);
                bodyRow.setHeightInPoints(18);

                Person person = persons.get(i);
                CellStyle style = (i % 2 == 0) ? bodyStyle : evenRowsCellStyle;

                Cell surnameCell = bodyRow.createCell(0);
                surnameCell.setCellValue(person.surname());
                surnameCell.setCellStyle(style);

                Cell nameCell = bodyRow.createCell(1);
                nameCell.setCellValue(person.name());
                nameCell.setCellStyle(style);

                Cell ageCell = bodyRow.createCell(2);
                ageCell.setCellValue(person.age());
                ageCell.setCellStyle(style);

                Cell phoneCell = bodyRow.createCell(3);
                phoneCell.setCellValue(person.phone());
                phoneCell.setCellStyle(style);
            }

            for (int i = 0; i < headerCells.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }

            sheet.createFreezePane(0, 1);

            workbook.write(outputStream);
        }
    }

    private static void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
    }
}
