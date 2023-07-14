package kazantseva.task.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VisionService {
    List<String> extractTextFromPdf(MultipartFile file);
}
