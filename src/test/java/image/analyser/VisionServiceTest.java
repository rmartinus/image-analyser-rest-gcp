package image.analyser;

import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        visionService = new VisionService(imageAnnotatorClient, cloudStorageService, uploadHistoryRepository, "myBucket");
    }

    @Test
    public void shouldAnalyseFileAndUploadItToStorageBucket() {
        BatchAnnotateImagesResponse batchAnnotateImagesResponse = mock(BatchAnnotateImagesResponse.class);
        given(batchAnnotateImagesResponse.toString()).willReturn("It is indeed an image of a dog");
        given(imageAnnotatorClient.batchAnnotateImages(anyList())).willReturn(batchAnnotateImagesResponse);
        byte[] fileContent = new byte[]{};

        String analyse = visionService.analyse("dog.png", fileContent);

        assertThat(analyse).isEqualTo("It is indeed an image of a dog");
        verify(cloudStorageService).uploadFile("dog.png", fileContent, "myBucket");
        List<UploadHistoryEntity> data = uploadHistoryRepository.findByType("test");
        assertThat(data.size()).isEqualTo(1);
        assertThat(data.get(0).getFileName()).isEqualTo("dog.png");
        assertThat(data.get(0).getAnalysis()).isEqualTo("It is indeed an image of a dog");
    }
}
