package rs.ac.bg.etf.osrpavicevic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rs.ac.bg.etf.osrpavicevic.api.request.NewsCreateRequest;
import rs.ac.bg.etf.osrpavicevic.domain.News;
import rs.ac.bg.etf.osrpavicevic.entity.NewsEntity;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "createRequest.title")
    @Mapping(target = "content", source = "createRequest.content")
    @Mapping(target = "type", source = "createRequest.type")
    @Mapping(target = "dateTime", source = "createRequest.dateTime")
    @Mapping(target = "clicks", constant = "0L")
    @Mapping(target = "pinned", constant = "false")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "comments", ignore = true)
    NewsEntity fromCreateToEntity(NewsCreateRequest createRequest);

    @Mapping(target = "comments", ignore = true)
    News toDomain(NewsEntity newsEntity);
}
