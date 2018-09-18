package image.analyser;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.util.Collections.singletonList;

@Service
public class CloudStorageService {

    private Storage storage;

    public CloudStorageService(Storage storage) {
        this.storage = storage;
    }

    public String uploadFile(String originalFileName, byte[] fileContent, String bucketName) {
        String fileName = getFileNameBasedOnTime(originalFileName, LocalDateTime.now(ZoneId.of("UTC")));

        BlobInfo blobInfo =
                storage.create(
                        BlobInfo
                                .newBuilder(bucketName, fileName)
                                .setAcl(singletonList(Acl.of(User.ofAllUsers(), Role.READER)))
                                .build(),
                        fileContent);

        return blobInfo.getMediaLink();
    }

    @VisibleForTesting
    String getFileNameBasedOnTime(String originalFileName, LocalDateTime dt) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("-yyyy-MM-dd-HHmmssSSS");
        String dtString = dt.format(dtf);

        return originalFileName + dtString;
    }
}
