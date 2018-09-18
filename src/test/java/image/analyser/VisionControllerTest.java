package image.analyser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
public class VisionControllerTest {
    @Mock
    private VisionService visionService;

    @InjectMocks
    private VisionController visionController;

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
