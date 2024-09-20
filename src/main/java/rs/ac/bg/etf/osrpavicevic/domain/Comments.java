package rs.ac.bg.etf.osrpavicevic.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Comments {
    Long id;
    private SchoolUser user;
    private News news;
    private String content;
    private LocalDateTime commentCreatedDate;
    private String reply;
    private LocalDateTime replyCreatedDate;
    private boolean approved;
}
