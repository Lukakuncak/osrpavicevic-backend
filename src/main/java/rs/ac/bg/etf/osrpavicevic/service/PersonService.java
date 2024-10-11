package rs.ac.bg.etf.osrpavicevic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.etf.osrpavicevic.api.request.PersonRequest;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfPersons;
import rs.ac.bg.etf.osrpavicevic.domain.Image;
import rs.ac.bg.etf.osrpavicevic.domain.Person;
import rs.ac.bg.etf.osrpavicevic.entity.ImageEntity;
import rs.ac.bg.etf.osrpavicevic.entity.PersonEntity;
import rs.ac.bg.etf.osrpavicevic.mapper.PersonMapper;
import rs.ac.bg.etf.osrpavicevic.respository.PersonRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final CloudinaryService cloudinaryService;
    private final PersonMapper personMapper;

    public List<Person> getAllForType(TypeOfPersons type) {
        return personRepository.getAllByType(type).stream().map(personMapper::toDomain).toList();
    }

    @Transactional
    public Person createNewPerson(PersonRequest personRequest) {
        PersonEntity personEntity = personMapper.toEntity(personRequest);
        return personMapper.toDomain(personRepository.save(personEntity));
    }

    @Transactional
    public void editPerson(PersonRequest personRequest, Integer id) {
        Optional<PersonEntity> personEntityOptional = personRepository.findById(id);
        if (personEntityOptional.isPresent()) {
            PersonEntity personEntity = personEntityOptional.get();
            personEntity.setFirstname(personRequest.firstname() != null ? personRequest.firstname() : personEntity.getFirstname());
            personEntity.setLastname(personRequest.lastname() != null ? personRequest.lastname() : personEntity.getLastname());
            personEntity.setPosition(personRequest.position() != null ? personRequest.position() : personEntity.getPosition());
            personRepository.save(personEntity);
        } else {
            throw new RuntimeException("Person with id not found: " + id);
        }
    }

    @Transactional
    public void deletePerson(Integer id) throws IOException {
        Optional<PersonEntity> personEntityOptional = personRepository.findById(id);
        if (personEntityOptional.isPresent()) {
            if(personEntityOptional.get().getImage()!=null){
                deletePictureForPerson(id);
            }
            personRepository.delete(personEntityOptional.get());
        } else {
            throw new RuntimeException("Person with id not found: " + id);
        }
    }


    @Transactional
    public void addImageToPerson(Integer id, MultipartFile multipartFile) throws IOException {
        Optional<PersonEntity> personEntityOptional = personRepository.findById(id);
        if (personEntityOptional.isPresent()) {
            PersonEntity personEntity = personEntityOptional.get();
            Image image = cloudinaryService.uploadImage(multipartFile);
            personEntity.setImage(ImageEntity.builder().id(image.getId())
                    .imageId(image.getImageId())
                    .imageUrl(image.getImageUrl())
                    .name(image.getName()).build());
            personRepository.save(personEntity);
        } else {
            throw new RuntimeException("Person with id not found: " + id);
        }
    }

    @Transactional
    public void deletePictureForPerson(Integer id) throws IOException {
        Optional<PersonEntity> personEntityOptional = personRepository.findByIdWithImage(id);
        if (personEntityOptional.isPresent()) {
            PersonEntity personEntity = personEntityOptional.get();
            ImageEntity imageEntity = personEntity.getImage();
            if (imageEntity == null) throw new RuntimeException("You cant delete image if it does not exists.");
            cloudinaryService.delete(imageEntity.getId());
            personEntity.setImage(null);
            personRepository.save(personEntity);
        } else {
            throw new RuntimeException("Person with id not found: " + id);
        }
    }
}
