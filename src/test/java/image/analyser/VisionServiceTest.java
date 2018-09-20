package image.analyser;

import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class VisionServiceTest {
    @MockBean
    private ImageAnnotatorClient imageAnnotatorClient;
    @MockBean
    private CloudStorageService cloudStorageService;
    @MockBean
    private UploadHistoryRepository uploadHistoryRepository;

    private VisionService visionService;

    @Before
    public void setUp() {
        visionService = new VisionService(imageAnnotatorClient, cloudStorageService, uploadHistoryRepository, "myBucket");
    }

    @Test
    public void shouldAnalyseFileAndUploadItToStorageBucket() {
        BatchAnnotateImagesResponse batchAnnotateImagesResponse = mock(BatchAnnotateImagesResponse.class);
        given(batchAnnotateImagesResponse.toString()).willReturn("it is indeed an image of a dog");
        given(imageAnnotatorClient.batchAnnotateImages(anyList())).willReturn(batchAnnotateImagesResponse);
        byte[] fileContent = new byte[]{};

        String analyse = visionService.analyse("dog.png", fileContent);

        assertThat(analyse).isEqualTo("it is indeed an image of a dog");
        verify(cloudStorageService).uploadFile("dog.png", fileContent, "myBucket");
    }
}
