package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.etf.osrpavicevic.api.request.PersonRequest;
import rs.ac.bg.etf.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.PersonsResponse;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfPersons;
import rs.ac.bg.etf.osrpavicevic.service.PersonService;

@RestController
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping("public/person/get-all-for-type/{type}")
    public ResponseEntity<PersonsResponse> getAllPersonsForType(@PathVariable("type") TypeOfPersons type) {
        try {
            return ResponseEntity.ok(PersonsResponse.builder().statusCode(200).message("Successfully fetched all persons for type")
                    .personList(personService.getAllForType(type)).build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(PersonsResponse.builder().error(e.getMessage())
                    .statusCode(500).build());
        }
    }

    @PostMapping("person/create-person")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> createNewPerson(@RequestBody PersonRequest personRequest) {
        try {
            personService.createNewPerson(personRequest);
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message("Successfully created person.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(DefaultResponse.builder().error(e.getMessage()).statusCode(500).build());
        }
    }

    @PutMapping("person/edit-person/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> editPerson(@RequestBody PersonRequest personRequest, @PathVariable("id") Integer id) {
        try {
            personService.editPerson(personRequest, id);
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message("Successfully edited person.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(DefaultResponse.builder().error(e.getMessage()).statusCode(500).build());
        }
    }
    @DeleteMapping("person/delete-person/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> deletePerson(@PathVariable("id") Integer id) {
        try {
            personService.deletePerson(id);
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message("Successfully deleted person.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(DefaultResponse.builder().error(e.getMessage()).statusCode(500).build());
        }
    }

    @PutMapping("person/add-picture/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> addPictureForPerson(@PathVariable("id") Integer id, @RequestParam MultipartFile multipartFile){
        try {
            personService.addImageToPerson(id, multipartFile);
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message("Successfully added picture to person.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(DefaultResponse.builder().error(e.getMessage()).statusCode(500).build());
        }
    }

    @PutMapping("person/delete-picture/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> deletePictureForPerson(@PathVariable("id") Integer id){
        try {
            personService.deletePictureForPerson(id);
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message("Successfully deleted picture for person.")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(DefaultResponse.builder().error(e.getMessage()).statusCode(500).build());
        }
    }

}
