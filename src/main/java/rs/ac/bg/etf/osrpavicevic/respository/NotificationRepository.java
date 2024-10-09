package rs.ac.bg.etf.osrpavicevic.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.etf.osrpavicevic.entity.NotificationEntity;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserIdAndViewedFalse(Integer userId);
}
