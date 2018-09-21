package image.analyser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadHistoryEntity extends AbstractEntity {
    private String type;
    private String fileName;
    private String fileLocation;
    @Lob private String analysis;
}
