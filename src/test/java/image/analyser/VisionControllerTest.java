package image.analyser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class VisionControllerTest {
    @MockBean
    private VisionService visionService;

    private VisionController visionController;

    @Before
    public void setUp() {
        visionController = new VisionController(visionService);
    }

    @Test
    public void shouldReturnAnalysisSuccessfullyWhenFileIsUploaded() throws IOException {
        MultipartFile image = mock(MultipartFile.class);
        byte[] imageBytes = "golden retriever".getBytes();
        given(image.getBytes()).willReturn(imageBytes);
        given(visionService.analyse(image.getOriginalFilename(), imageBytes)).willReturn("Dog, mammal, golden retriever");

        String analysis = visionController.analyse(image);

        assertThat(analysis).isEqualTo("Dog, mammal, golden retriever");
    }
}
