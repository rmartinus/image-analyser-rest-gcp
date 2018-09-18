package image.analyser;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CloudStorageServiceTest {
    @Mock
    private Storage storage;
    @InjectMocks
    private CloudStorageService cloudStorageService;

    @Test
    public void shouldCreateFileNameBasedOnTime() {
        LocalDateTime dt = LocalDateTime.of(2020, 10, 1, 11, 12, 13, 14);
        String fileName = cloudStorageService.getFileNameBasedOnTime("dog", dt);

        assertThat(fileName).isEqualTo("dog-2020-10-01-111213000");
    }

    @Test
    public void shouldUploadFile() {
        Blob blob = mock(Blob.class);
        byte[] fileContent = new byte[]{'1', '2', '3'};
        given(storage.create(any(BlobInfo.class), eq(fileContent))).willReturn(blob);

        cloudStorageService.uploadFile("dog.png", fileContent, "mybucket");

        verify(blob).getMediaLink();
    }
}
