package gcp;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.google.cloud.vision.v1.Feature.Type.DOCUMENT_TEXT_DETECTION;
import static com.google.cloud.vision.v1.Feature.Type.LABEL_DETECTION;
import static java.util.Collections.singletonList;

@RestController
public class VisionController {
    private final ImageAnnotatorClient imageAnnotatorClient;

    VisionController(ImageAnnotatorClient imageAnnotatorClient) {
        this.imageAnnotatorClient = imageAnnotatorClient;
    }

    @PostMapping("/analyse")
    public String analyse(@RequestParam MultipartFile image) throws IOException {
        BatchAnnotateImagesResponse batchAnnotateImagesResponse = imageAnnotatorClient.batchAnnotateImages(
                singletonList(AnnotateImageRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(LABEL_DETECTION).build())
                        .addFeatures(Feature.newBuilder().setType(DOCUMENT_TEXT_DETECTION).build())
                        .setImage(Image.newBuilder()
                                .setContent(ByteString.copyFrom(image.getBytes()))
                                .build())
                        .build())
        );

        return batchAnnotateImagesResponse.toString();
    }
}
