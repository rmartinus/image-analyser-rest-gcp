package image.analyser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class VisionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisionController.class);
    private final VisionService visionService;

    public VisionController(VisionService visionService) {
        this.visionService = visionService;
    }

    @PostMapping(value = "/v1/analyse")
    @ResponseBody
    public Map<String, Float> analyse(@RequestParam MultipartFile image) throws IOException {
        LOGGER.info("Received uploaded file {}", image.getOriginalFilename());
        Map<String, Float> analysis = visionService.analyse(image.getOriginalFilename(), image.getBytes());
        return analysis;
    }
}
