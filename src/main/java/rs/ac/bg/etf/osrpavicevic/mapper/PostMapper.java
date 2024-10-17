package rs.ac.bg.etf.osrpavicevic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rs.ac.bg.etf.osrpavicevic.api.request.PostCreateRequest;
import rs.ac.bg.etf.osrpavicevic.domain.Post;
import rs.ac.bg.etf.osrpavicevic.entity.PostEntity;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "createRequest.title")
    @Mapping(target = "content", source = "createRequest.content")
    @Mapping(target = "type", source = "createRequest.type")
    @Mapping(target = "dateTime", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "file", ignore = true)
    PostEntity fromCreateToEntity(PostCreateRequest createRequest);

    Post toDomain(PostEntity newsEntity);
}
