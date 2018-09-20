package image.analyser;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class UploadHistoryEntity extends AbstractEntity {
    private String type;
}
