package kazantseva.task.service.impl;

import com.google.cloud.spring.vision.CloudVisionTemplate;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import kazantseva.task.service.VisionService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class VisionServiceImpl implements VisionService {

    private final String[] keywords = {"الاهميه الاقتصادية", "الإنتاجية", "التربة", "التعشيب", "الأسمدة", "أعراض", "الوقاية"};
    private CloudVisionTemplate cloudVisionTemplate;

    @Override
    public List<String> extractTextFromPdf(MultipartFile file) {
        List<String> texts =
                cloudVisionTemplate.extractTextFromPdf(file.getResource());

        var importantInfo = extractImportantInfo(String.join(" ", texts));

        toExcel(importantInfo, file.getOriginalFilename());

        return texts;
    }

    public void toExcel(Map<String, List<String>> text, String fileName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        int numCell = 0;

        Row mainRow = sheet.createRow(0);
        Row dataRow = sheet.createRow(1);

        for (String k : keywords) {
            Cell cell = mainRow.createCell(numCell);
            cell.setCellValue(k);
            cell.setCellStyle(mainRowStyle(workbook));

            Cell dataCell = dataRow.createCell(numCell);
            dataCell.setCellValue(String.join(" ", text.get(k)));
            dataCell.setCellStyle(dataRowStyle(workbook));
            numCell++;
        }

        mainRow.setHeight((short) 800);
        dataRow.setHeight((short) 7000);

        for (int i = 0; i < numCell; i++) {
            sheet.autoSizeColumn(i);
        }

        String xlsxFileName = fileName.split("\\.")[0] + ".xlsx";

        try (FileOutputStream outputStream = new FileOutputStream(xlsxFileName)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, List<String>> extractImportantInfo(String text) {
        Properties props = new Properties();

        props.setProperty("annotators", "tokenize, ssplit");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Map<String, List<String>> result = new HashMap<>();

        for (String keyword : keywords) {
            List<String> relevantSentences = new ArrayList<>();

            Annotation annotation = new Annotation(text);

            pipeline.annotate(annotation);

            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

            for (CoreMap sentence : sentences) {
                String sentenceText = sentence.get(CoreAnnotations.TextAnnotation.class);

                if (sentenceText.toLowerCase().contains(keyword.toLowerCase())) {
                    relevantSentences.add(sentenceText);
                }
            }
            result.put(keyword, relevantSentences);
        }
        return result;
    }

    private CellStyle mainRowStyle(Workbook workbook) {
        CellStyle style;

        XSSFFont font = (XSSFFont) workbook.createFont();

        font.setFontHeightInPoints((short) 12);
        font.setFontName("San Serif");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        font.setItalic(false);

        style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.DIAMONDS);

        style.setAlignment(HorizontalAlignment.CENTER);

        style.setFont(font);
        style.setWrapText(true);

        return style;
    }

    private CellStyle dataRowStyle(Workbook workbook) {
        CellStyle style;

        XSSFFont font = (XSSFFont) workbook.createFont();

        font.setFontHeightInPoints((short) 12);
        font.setFontName("San Serif");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(false);
        font.setItalic(false);

        style = workbook.createCellStyle();

        style.setFont(font);
        style.setWrapText(true);

        return style;
    }
}
