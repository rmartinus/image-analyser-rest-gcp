package image.analyser;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.annotations.VisibleForTesting;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.util.Collections.singletonList;

public class CloudStorageService {

    private static Storage storage = null;

    static {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public String uploadFile(String originalFileName, byte[] fileContent, String bucketName) throws IOException {
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
