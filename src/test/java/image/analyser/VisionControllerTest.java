package image.analyser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(VisionController.class)
public class VisionControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private VisionService visionService;

    private VisionController visionController;

    @Before
    public void setUp() {
        visionController = new VisionController(visionService);
    }

    @Test
    public void shouldReturnAnalysisSuccessfullyWhenFileIsUploaded() throws Exception {
        byte[] imageBytes = "golden retriever".getBytes();
        MockMultipartFile file = new MockMultipartFile("image", "dog.jpg", "image/jpg", imageBytes);
        given(visionService.analyse("dog.jpg", imageBytes)).willReturn("Dog, mammal, golden retriever");

        mvc.perform(MockMvcRequestBuilders.multipart("/analyse")
                .file(file))
                .andExpect(status().is(200))
                .andExpect(content().string("Dog, mammal, golden retriever"));
    }
}
