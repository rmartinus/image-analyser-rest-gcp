package image.analyser;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VisionServiceTest {
    @MockBean
    private ImageAnnotatorClient imageAnnotatorClient;
    @MockBean
    private CloudStorageService cloudStorageService;
    @Autowired
    private UploadHistoryRepository uploadHistoryRepository;

    private VisionService visionService;

    @Before
    public void setUp() {
        visionService = new VisionService(cloudStorageService, imageAnnotatorClient, uploadHistoryRepository, "myBucket");
    }

    @Test
    public void shouldUploadImageToStorageBucketThenAnalyseFileThenStoreInDB() {
        BatchAnnotateImagesResponse batchAnnotateImagesResponse = BatchAnnotateImagesResponse.newBuilder()
                .addResponses((AnnotateImageResponse.newBuilder()
                        .addLabelAnnotations(
                                EntityAnnotation.newBuilder()
                                        .setMid("/m/036qh8")
                                        .setDescription("produce")
                                        .setScore(0.9113305F)
                                        .setTopicality(0.9113305F)
                                        .build())
                        .addLabelAnnotations(
                                EntityAnnotation.newBuilder()
                                        .setMid("/m/0fldg")
                                        .setDescription("mango")
                                        .setScore(0.89417756F)
                                        .setTopicality(0.89417756F)
                                        .build())
                        .build()))
                .build();
        given(cloudStorageService.uploadFile(anyString(), any(), anyString())).willReturn("mediaLink");
        given(imageAnnotatorClient.batchAnnotateImages(anyList())).willReturn(batchAnnotateImagesResponse);
        byte[] fileContent = new byte[]{};

        Map<String, Float> analyse = visionService.analyse("mango.png", fileContent);

        InOrder inOrder = inOrder(cloudStorageService, imageAnnotatorClient);
        inOrder.verify(cloudStorageService).uploadFile("mango.png", fileContent, "myBucket");
        inOrder.verify(imageAnnotatorClient).batchAnnotateImages(anyList());

        Map<String, Float> expectedOutput = new HashMap<>();
        expectedOutput.put("produce", 0.9113305F);
        expectedOutput.put("mango", 0.89417756F);

        assertThat(analyse).isEqualTo(expectedOutput);
        List<UploadHistoryEntity> data = uploadHistoryRepository.findByType("test");
        assertThat(data.size()).isEqualTo(1);
        assertThat(data.get(0).getFileName()).isEqualTo("mango.png");
        assertThat(data.get(0).getFileLocation()).isEqualTo("mediaLink");
        assertThat(data.get(0).getAnalysis()).isEqualTo(expectedOutput.toString());
    }
}
