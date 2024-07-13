package rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.entity.SchoolUserEntity;

@Data
@SuperBuilder
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationResponse extends DefaultResponse{
    private SchoolUserEntity schoolUserEntity;
}
