package rs.ac.bg.etf.osrpavicevic.api.request;

import lombok.Builder;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;

import java.time.LocalDateTime;

@Builder
public record NewsCreateRequest(
        String title,
        String content,
        TypeOfNews type,
        LocalDateTime dateTime
) {
}
