package rs.ac.bg.etf.osrpavicevic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.bg.etf.osrpavicevic.constants.ClassScheduleSchool;

@Entity
@Table(name = "class_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassScheduleEntity {
    @Id
    @Column(nullable = false)
    private ClassScheduleSchool nameOfSchool;
    @Column(nullable = false)
    private String url;
}
