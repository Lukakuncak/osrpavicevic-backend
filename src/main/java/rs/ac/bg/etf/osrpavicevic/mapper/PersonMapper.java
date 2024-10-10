package rs.ac.bg.etf.osrpavicevic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rs.ac.bg.etf.osrpavicevic.api.request.PersonRequest;
import rs.ac.bg.etf.osrpavicevic.domain.Person;
import rs.ac.bg.etf.osrpavicevic.entity.PersonEntity;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person toDomain(PersonEntity personEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    PersonEntity toEntity(PersonRequest personRequest);
}
