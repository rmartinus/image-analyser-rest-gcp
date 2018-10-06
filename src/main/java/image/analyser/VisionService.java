package image.analyser;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.cloud.vision.v1.Feature.Type.*;
import static java.util.Collections.singletonList;

@Service
public class VisionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisionService.class);
    private final ImageAnnotatorClient imageAnnotatorClient;
    private final CloudStorageService cloudStorageService;
    private final String bucketName;
    private final UploadHistoryRepository uploadHistoryRepository;

    public VisionService(CloudStorageService cloudStorageService,
                         ImageAnnotatorClient imageAnnotatorClient,
                         UploadHistoryRepository uploadHistoryRepository,
                         @Value("${image.analyser.cloud.storage.bucket.name}") String bucketName) {
        this.imageAnnotatorClient = imageAnnotatorClient;
        this.cloudStorageService = cloudStorageService;
        this.uploadHistoryRepository = uploadHistoryRepository;
        this.bucketName = bucketName;
    }

    public Map<String, Float> analyse(String fileName, byte[] fileContent) {
        String mediaLink = storeFile(fileName, fileContent);
        BatchAnnotateImagesResponse batchAnnotateImagesResponse = analyseFile(fileName, fileContent);
        Map<String, Float> response = new LinkedHashMap<>();
        batchAnnotateImagesResponse.getResponsesList().forEach(imageResponse -> {
            imageResponse.getLandmarkAnnotationsList().forEach(landmarkAnnotation -> response.put(landmarkAnnotation.getDescription(), landmarkAnnotation.getScore()));
            imageResponse.getLogoAnnotationsList().forEach(logoAnnotation -> response.put(logoAnnotation.getDescription(), logoAnnotation.getScore()));
            imageResponse.getLabelAnnotationsList().forEach(labelAnnotation -> response.put(labelAnnotation.getDescription(), labelAnnotation.getScore()));
            imageResponse.getTextAnnotationsList().forEach(textAnnotation -> response.put(textAnnotation.getDescription(), textAnnotation.getScore()));
        });

        saveImageInformation(fileName, mediaLink, response);
        return response;
    }

    private String storeFile(String fileName, byte[] fileContent) {
        LOGGER.debug("Storing file {} in {} bucket....", fileName, bucketName);
        String mediaLink = cloudStorageService.uploadFile(fileName, fileContent, bucketName);
        LOGGER.debug("Store completed");

        return mediaLink;
    }

    private BatchAnnotateImagesResponse analyseFile(String fileName, byte[] fileContent) {
        LOGGER.debug("Analysing file {}....", fileName);
        BatchAnnotateImagesResponse batchAnnotateImagesResponse = imageAnnotatorClient.batchAnnotateImages(
                singletonList(AnnotateImageRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(LANDMARK_DETECTION).build())
                        .addFeatures(Feature.newBuilder().setType(LOGO_DETECTION).build())
                        .addFeatures(Feature.newBuilder().setType(LABEL_DETECTION).build())
                        .addFeatures(Feature.newBuilder().setType(TEXT_DETECTION).build())
                        .addFeatures(Feature.newBuilder().setType(DOCUMENT_TEXT_DETECTION).build())
                        .setImage(Image.newBuilder()
                                .setContent(ByteString.copyFrom(fileContent))
                                .build())
                        .build())
        );
        LOGGER.debug("Analysis completed. Result: {}", batchAnnotateImagesResponse.toString());

        return batchAnnotateImagesResponse;
    }

    private void saveImageInformation(String fileName, String mediaLink, Map<String, Float> response) {
        LOGGER.debug("Saving it to db");
        Set<String> typeSet = response.keySet().stream()
                .filter(key -> key.equalsIgnoreCase(ImageType.DOG.name()) || key.equalsIgnoreCase(ImageType.CAT.name()))
                .collect(Collectors.toSet());
        String type = typeSet.size() > 0 ? typeSet.iterator().next().toUpperCase() : ImageType.GENERAL.name();
        uploadHistoryRepository.save(new UploadHistoryEntity(type, fileName, mediaLink, response.toString()));
        LOGGER.debug("Save completed");
    }
}
