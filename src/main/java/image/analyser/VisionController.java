package image.analyser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class VisionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisionController.class);
    private final VisionService visionService;

    public VisionController(VisionService visionService) {
        this.visionService = visionService;
    }

    @PostMapping("/v1/analyse")
    public String analyse(@RequestParam MultipartFile image) throws IOException {
        LOGGER.info("Received uploaded file {}", image.getOriginalFilename());
        return visionService.analyse(image.getOriginalFilename(), image.getBytes());
    }
}
