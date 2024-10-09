package rs.ac.bg.etf.osrpavicevic.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class News {
    private Long id;
    private String title;
    private String content;
    private TypeOfNews type;
    private LocalDateTime dateTime;
    private Long clicks;
    private boolean pinned;
    private boolean deleted;
    private List<Comment> comments;
}
