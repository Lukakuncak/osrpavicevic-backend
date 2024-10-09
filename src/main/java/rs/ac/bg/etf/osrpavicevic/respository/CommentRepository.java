package rs.ac.bg.etf.osrpavicevic.respository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.bg.etf.osrpavicevic.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByApprovedFalse();


    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT c FROM CommentEntity c WHERE c.id = :id")
    Optional<CommentEntity> findByIdWithUser(@Param("id") Long id);
}
