package image.analyser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadHistoryRepository extends JpaRepository<UploadHistoryEntity, Long> {
    List<UploadHistoryEntity> findByType(String type);
}
