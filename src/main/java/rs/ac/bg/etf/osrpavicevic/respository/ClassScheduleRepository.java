package rs.ac.bg.etf.osrpavicevic.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.etf.osrpavicevic.constants.ClassScheduleSchool;
import rs.ac.bg.etf.osrpavicevic.entity.ClassScheduleEntity;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassScheduleEntity, ClassScheduleSchool> {

}
