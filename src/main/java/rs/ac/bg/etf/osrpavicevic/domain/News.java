package rs.ac.bg.etf.osrpavicevic.domain;

import lombok.Builder;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;

import java.time.LocalDateTime;

@Builder
public record News(Long id,
                   String title,
                   String content,
                   TypeOfNews type,
                   String author,
                   LocalDateTime dateTime,
                   Long clicks,
                   boolean pinned) {
}
