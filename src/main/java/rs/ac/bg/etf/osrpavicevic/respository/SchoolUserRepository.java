package rs.ac.bg.etf.osrpavicevic.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.etf.osrpavicevic.entity.SchoolUserEntity;

import java.util.Optional;

@Repository
public interface SchoolUserRepository extends JpaRepository<SchoolUserEntity, Integer> {

    Optional<SchoolUserEntity> findByUsername(String username);

    Page<SchoolUserEntity> findByUsernameContainingIgnoreCase(String search, Pageable pageable);
}
