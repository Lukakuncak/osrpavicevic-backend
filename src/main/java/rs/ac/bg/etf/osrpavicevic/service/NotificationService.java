package rs.ac.bg.etf.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.api.response.SingleNotificationResponse;
import rs.ac.bg.etf.osrpavicevic.entity.NotificationEntity;
import rs.ac.bg.etf.osrpavicevic.entity.SchoolUserEntity;
import rs.ac.bg.etf.osrpavicevic.mapper.NotificationMapper;
import rs.ac.bg.etf.osrpavicevic.respository.NotificationRepository;
import rs.ac.bg.etf.osrpavicevic.respository.SchoolUserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final String USER_NOT_FOUND = "User not found!";

    private final NotificationRepository notificationRepository;
    private final SchoolUserRepository schoolUserRepository;
    private final NotificationMapper notificationMapper;

    public List<SingleNotificationResponse> getNotificationWithComments(String username) {
        SchoolUserEntity user = schoolUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        List<NotificationEntity> notificationEntities = notificationRepository.findByUserIdWithCommentsAndViewedFalse(user.getId());
        return notificationEntities.stream().map(notificationMapper::toDomain).toList();
    }

    public void viewNotification(String username, Long id) {
        SchoolUserEntity user = schoolUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        notificationRepository.updateNotificationAsViewed(user.getId(),id);
    }

    public void viewAllNotifications(String username) {
        SchoolUserEntity user = schoolUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        notificationRepository.updateAllNotificationsAsViewed(user.getId());
    }
}
