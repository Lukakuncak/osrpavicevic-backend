package rs.ac.bg.etf.osrpavicevic.mapper;

import org.mapstruct.Mapper;
import rs.ac.bg.etf.osrpavicevic.domain.SchoolUser;
import rs.ac.bg.etf.osrpavicevic.entity.SchoolUserEntity;

@Mapper(componentModel = "spring")
public interface SchoolUserMapper {

    SchoolUser toDomain(SchoolUserEntity schoolUserEntity);
}
