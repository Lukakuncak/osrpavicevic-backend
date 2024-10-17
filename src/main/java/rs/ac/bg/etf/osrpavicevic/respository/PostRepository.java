package rs.ac.bg.etf.osrpavicevic.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.etf.osrpavicevic.constants.PostType;
import rs.ac.bg.etf.osrpavicevic.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findAllByTypeAndDeletedFalse(PostType postType, Pageable pageable);

    Page<PostEntity> findAllByTitleContainingIgnoreCaseAndTypeAndDeletedFalse(String search, PostType postType, Pageable pageable);
}
