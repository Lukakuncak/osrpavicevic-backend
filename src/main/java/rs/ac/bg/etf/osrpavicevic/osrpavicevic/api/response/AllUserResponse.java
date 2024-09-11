package rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.entity.SchoolUserEntity;

import java.util.List;

@Data
@SuperBuilder
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllUserResponse  extends DefaultResponse{
    List<SchoolUserEntity> users;
    Integer totalPages;
    long totalElements;
}
