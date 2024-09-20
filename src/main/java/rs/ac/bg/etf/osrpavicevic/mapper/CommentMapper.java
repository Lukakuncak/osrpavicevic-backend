package rs.ac.bg.etf.osrpavicevic.mapper;

import org.mapstruct.Mapper;
import rs.ac.bg.etf.osrpavicevic.domain.Comments;
import rs.ac.bg.etf.osrpavicevic.entity.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comments toDomain(CommentEntity commentEntity);
}
