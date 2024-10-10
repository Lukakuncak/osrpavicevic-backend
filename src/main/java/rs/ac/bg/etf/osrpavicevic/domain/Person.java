package rs.ac.bg.etf.osrpavicevic.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Person {
    private Integer id;
    private String firstname;
    private String lastname;
    private String position;
    private Image image;
}
