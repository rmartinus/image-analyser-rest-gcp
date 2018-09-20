package image.analyser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadHistoryRepository extends JpaRepository<UploadHistoryEntity, Long> {
    List<UploadHistoryEntity> findByType(String type);
}
