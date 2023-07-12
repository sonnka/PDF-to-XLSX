package kazantseva.task.controller;

import kazantseva.task.service.VisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class VisionController {

    @Autowired
    private VisionService visionService;

    //Extract the text in an image
    @PostMapping("/extractTextFromImage")
    public String extractTextFromImage(
            @RequestParam MultipartFile file) {

        return visionService.extractTextFromImage(file);

    }

    //Extract the text in a pdf
    @PostMapping("/extractTextFromPdf")
    public List<String> extractTextFromPdf(
            @RequestParam MultipartFile file) {

        return visionService.extractTextFromPdf(file);
    }
}