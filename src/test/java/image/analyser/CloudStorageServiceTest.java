package image.analyser;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CloudStorageServiceTest {

    private CloudStorageService cloudStorageService = new CloudStorageService();

    @Test
    public void shouldCreateFileNameBasedOnTime() throws IOException {
        LocalDateTime dt = LocalDateTime.of(2020, 10, 1, 11, 12, 13, 14);
        String fileName = cloudStorageService.getFileNameBasedOnTime("dog", dt);

        assertThat(fileName).isEqualTo("dog-2020-10-01-111213000");
    }
}
