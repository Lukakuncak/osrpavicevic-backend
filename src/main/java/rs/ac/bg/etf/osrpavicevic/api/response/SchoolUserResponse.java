package rs.ac.bg.etf.osrpavicevic.api.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import rs.ac.bg.etf.osrpavicevic.domain.SchoolUser;


@SuperBuilder
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolUserResponse extends DefaultResponse{
    private SchoolUser schoolUser;
}
