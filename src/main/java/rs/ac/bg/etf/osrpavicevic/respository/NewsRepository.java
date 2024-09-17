package rs.ac.bg.etf.osrpavicevic.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;
import rs.ac.bg.etf.osrpavicevic.entity.NewsEntity;

import java.util.List;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    Page<NewsEntity> findAllByType(TypeOfNews type, Pageable pageable);
    Page<NewsEntity> findAllByTypeAndTitleContainingIgnoreCase(TypeOfNews type, String title, Pageable pageable);
    Page<NewsEntity> findAllByTitleContainingIgnoreCase(String search, Pageable pageable);
}
