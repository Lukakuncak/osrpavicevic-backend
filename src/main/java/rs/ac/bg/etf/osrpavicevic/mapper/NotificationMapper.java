package rs.ac.bg.etf.osrpavicevic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rs.ac.bg.etf.osrpavicevic.api.response.SingleNotificationResponse;
import rs.ac.bg.etf.osrpavicevic.domain.Comment;
import rs.ac.bg.etf.osrpavicevic.domain.News;
import rs.ac.bg.etf.osrpavicevic.entity.CommentEntity;
import rs.ac.bg.etf.osrpavicevic.entity.NewsEntity;
import rs.ac.bg.etf.osrpavicevic.entity.NotificationEntity;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    SingleNotificationResponse toDomain(NotificationEntity notification);

    @Mapping(target = "user", ignore = true)
    Comment toDomain(CommentEntity commentEntity);

    @Mapping(target = "content", ignore = true)
    @Mapping(target = "type",ignore = true)
    @Mapping(target = "dateTime",ignore = true)
    @Mapping(target = "clicks",ignore = true)
    @Mapping(target = "pinned",ignore = true)
    @Mapping(target = "deleted",ignore = true)
    @Mapping(target = "comments",ignore = true)
    News toDomain(NewsEntity news);
}
