package rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchoolUserUpdateRequest {
    private String firstname;
    private String lastname;
}
