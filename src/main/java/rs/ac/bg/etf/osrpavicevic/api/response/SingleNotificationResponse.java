package rs.ac.bg.etf.osrpavicevic.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import rs.ac.bg.etf.osrpavicevic.domain.Comment;


@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleNotificationResponse {
    private Long id;
    private Comment comment;
}
