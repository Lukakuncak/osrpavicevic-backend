package rs.ac.bg.etf.osrpavicevic.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfPersons;
import rs.ac.bg.etf.osrpavicevic.entity.PersonEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {

    List<PersonEntity> getAllByType(TypeOfPersons type);
    @Query("SELECT p FROM PersonEntity p LEFT JOIN FETCH p.image WHERE p.id = :id")
    Optional<PersonEntity> findByIdWithImage(@Param("id") Integer id);

    @Query("SELECT count(*) FROM PersonEntity p WHERE p.type = :type")
    Integer getNumberOfSingleType(TypeOfPersons type);
}
