package kazantseva.task.controller;

import kazantseva.task.service.VisionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class VisionController {

    private VisionService visionService;

    @PostMapping("/extractTextFromPdf")
    public List<String> extractTextFromPdf(
            @RequestParam MultipartFile file) {
        return visionService.extractTextFromPdf(file);
    }
}