package rs.ac.bg.etf.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfPersons;
import rs.ac.bg.etf.osrpavicevic.respository.PersonRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HomePageService {
    private final static Integer YEAR_OF_START = 1853;
    private final PersonRepository personRepository;

    public Integer getHowLongSchoolWorks() {
        Integer todaysYear = LocalDateTime.now().getYear();
        return todaysYear - YEAR_OF_START;
    }

    public Integer getNumberOfWorkers() {
        return personRepository.getNumberOfSingleType(TypeOfPersons.ZAPOSLENI);
    }
}
