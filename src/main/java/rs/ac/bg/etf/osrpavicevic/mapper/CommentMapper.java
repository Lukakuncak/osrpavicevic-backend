package rs.ac.bg.etf.osrpavicevic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rs.ac.bg.etf.osrpavicevic.api.request.CommentRequest;
import rs.ac.bg.etf.osrpavicevic.domain.Comments;
import rs.ac.bg.etf.osrpavicevic.entity.CommentEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "news",ignore = true)
    List<Comments> toDomain(List<CommentEntity> commentEntities);

    @Mapping(target = "news",ignore = true)
    Comments toDomain(CommentEntity commentEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "news", ignore = true)
    @Mapping(target = "commentCreatedDate", ignore = true)
    @Mapping(target = "reply", ignore = true)
    @Mapping(target = "replyCreatedDate", ignore = true)
    @Mapping(target = "approved", constant = "false")
    CommentEntity fromCreateRequest(CommentRequest commentRequest);
}
