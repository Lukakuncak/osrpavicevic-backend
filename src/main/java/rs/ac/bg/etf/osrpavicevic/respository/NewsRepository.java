package rs.ac.bg.etf.osrpavicevic.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;
import rs.ac.bg.etf.osrpavicevic.entity.NewsEntity;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    Page<NewsEntity> findAllByPinned(Boolean pinned, Pageable pageable);
    Page<NewsEntity> findAllByType(TypeOfNews type, Pageable pageable);
    Page<NewsEntity> findAllByTypeAndTitleContainingIgnoreCase(TypeOfNews type, String title, Pageable pageable);
    Page<NewsEntity> findAllByTitleContainingIgnoreCase(String search, Pageable pageable);

    @Query("SELECT n FROM NewsEntity n LEFT JOIN FETCH n.comments WHERE n.id = :id")
    Optional<NewsEntity> findByIdWithComments(@Param("id") Long id);
}
