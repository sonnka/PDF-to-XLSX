package kazantseva.task.service.impl;

import com.google.cloud.spring.vision.CloudVisionTemplate;
import kazantseva.task.service.VisionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        
        return texts;
    }
}
