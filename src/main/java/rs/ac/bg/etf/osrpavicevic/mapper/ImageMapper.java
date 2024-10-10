package rs.ac.bg.etf.osrpavicevic.mapper;

import org.mapstruct.Mapper;
import rs.ac.bg.etf.osrpavicevic.domain.Image;
import rs.ac.bg.etf.osrpavicevic.entity.ImageEntity;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    Image toDomain(ImageEntity image);
}
