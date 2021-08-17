package hospital.services.hospitalList.excel;

import hospital.domain.HospitalList;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class ExcelFileExporter {
    static DateTimeFormatter formatterDate = DateTimeFormatter.ISO_LOCAL_DATE;

    public ByteArrayInputStream callsListToExcelFile(List<HospitalList> hospitalLists) {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Зведена лікарняна виписка пацієнта.");

            Row row = sheet.createRow(0);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell = row.createCell(0);
            cell.setCellValue("Дата початку лікування");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1);
            cell.setCellValue("Первинний діагноз");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(2);
            cell.setCellValue("Дата виписки");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(3);
            cell.setCellValue("Фінальний діагноз");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(4);
            cell.setCellValue("Прописані ліки");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(5);
            cell.setCellValue("Призначені операції");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(6);
            cell.setCellValue("Лікар");
            cell.setCellStyle(headerCellStyle);

            // Creating data rows for each hospitalLists
            for(int i = 0; i < hospitalLists.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(hospitalLists.get(i).getDateCreate().format(formatterDate));
                dataRow.createCell(1).setCellValue(hospitalLists.get(i).getPrimaryDiagnosis());
                dataRow.createCell(2).setCellValue(hospitalLists.get(i).getDateDischarge().format(formatterDate));
                dataRow.createCell(3).setCellValue(hospitalLists.get(i).getFinalDiagnosis());
                dataRow.createCell(4).setCellValue(hospitalLists.get(i).getMedicine());
                dataRow.createCell(5).setCellValue(hospitalLists.get(i).getOperations());
                dataRow.createCell(6).setCellValue(hospitalLists.get(i).getDoctorName());
            }

            // Making size of column auto resize to fit with data
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
