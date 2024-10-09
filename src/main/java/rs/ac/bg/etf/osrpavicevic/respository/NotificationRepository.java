package rs.ac.bg.etf.osrpavicevic.respository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.bg.etf.osrpavicevic.entity.NotificationEntity;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserIdAndViewedFalse(Integer userId);

    @Query("SELECT n FROM NotificationEntity n LEFT JOIN FETCH n.comment WHERE n.user.id = :id and n.viewed = false")
    List<NotificationEntity> findByUserIdWithCommentsAndViewedFalse(@Param("id")Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE NotificationEntity n SET n.viewed = true WHERE n.user.id = :userId AND n.id = :notificationId")
    void updateNotificationAsViewed(@Param("userId") Integer userId, @Param("notificationId") Long notificationId);

    @Modifying
    @Transactional
    @Query("UPDATE NotificationEntity n SET n.viewed = true WHERE n.user.id = :userId")
    void updateAllNotificationsAsViewed(@Param("userId") Integer userId);
}
