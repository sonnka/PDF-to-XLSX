package kazantseva.task.service.impl;

import com.google.cloud.spring.vision.CloudVisionTemplate;
import kazantseva.task.service.VisionService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class VisionServiceImpl implements VisionService {

    private CloudVisionTemplate cloudVisionTemplate;

    @Override
    public String extractTextFromImage(MultipartFile file) {

        String textFromImage = cloudVisionTemplate.
                extractTextFromImage(file.getResource());

        return textFromImage;
    }

    @Override
    public List<String> extractTextFromPdf(MultipartFile file) {

        List<String> texts =
                cloudVisionTemplate.extractTextFromPdf(file.getResource());

        toExcel(texts.get(0), file.getName());

        return texts;
    }

    public void toExcel(String text, String fileName) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");


        String[] lines = text.split("\n");
        String importantData = lines[0];

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Extracted Data");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(importantData);

        String xlsxFileName = fileName.split("\\.")[0] + ".xlsx";

        try (FileOutputStream outputStream = new FileOutputStream(xlsxFileName)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
