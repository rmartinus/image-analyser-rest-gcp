package image.analyser;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.google.cloud.vision.v1.Feature.Type.*;
import static java.util.Collections.singletonList;

@Service
public class VisionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisionService.class);
    private final ImageAnnotatorClient imageAnnotatorClient;

    public VisionService(ImageAnnotatorClient imageAnnotatorClient) {
        this.imageAnnotatorClient = imageAnnotatorClient;
    }

    public String analyse(byte[] image) {
        LOGGER.info("Analysing....");
        BatchAnnotateImagesResponse batchAnnotateImagesResponse = imageAnnotatorClient.batchAnnotateImages(
                singletonList(AnnotateImageRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(FACE_DETECTION).build())
                        .addFeatures(Feature.newBuilder().setType(LANDMARK_DETECTION).build())
                        .addFeatures(Feature.newBuilder().setType(LOGO_DETECTION).build())
                        .addFeatures(Feature.newBuilder().setType(LABEL_DETECTION).build())
                        .addFeatures(Feature.newBuilder().setType(TEXT_DETECTION).build())
                        .addFeatures(Feature.newBuilder().setType(DOCUMENT_TEXT_DETECTION).build())
                        .setImage(Image.newBuilder()
                                .setContent(ByteString.copyFrom(image))
                                .build())
                        .build())
        );
        LOGGER.info("Analysis completed");

        return batchAnnotateImagesResponse.toString();
    }
}
