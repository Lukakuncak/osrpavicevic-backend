package rs.ac.bg.etf.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.api.request.SchoolUserUpdateRequest;
import rs.ac.bg.etf.osrpavicevic.api.response.SchoolUserResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.domain.SchoolUser;
import rs.ac.bg.etf.osrpavicevic.entity.SchoolUserEntity;
import rs.ac.bg.etf.osrpavicevic.mapper.SchoolUserMapper;
import rs.ac.bg.etf.osrpavicevic.respository.SchoolUserRepository;

@Service
@RequiredArgsConstructor
public class UserManagementService {
    private static final String USER_NOT_FOUND = "User not found!";
    private final SchoolUserRepository schoolUserRepository;
    private final SchoolUserMapper schoolUserMapper;
    private final AuthService authService;


    public Page<SchoolUser> getAllUsers(Pageable pageable, String search) {
        if (search == null || search.isEmpty()) {
            return schoolUserRepository.findAll(pageable).map(schoolUserMapper::toDomain);
        } else {
            return schoolUserRepository.findByUsernameContainingIgnoreCase(search, pageable)
                    .map(schoolUserMapper::toDomain);
        }
    }

    public SchoolUser getUserById(Integer id) {
        SchoolUserEntity user = schoolUserRepository.findById(id).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        return schoolUserMapper.toDomain(user);
    }

    public void deleteUser(Integer id) {
        SchoolUserEntity schoolUserEntity = schoolUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        schoolUserRepository.delete(schoolUserEntity);
    }


    public SchoolUser updateUser(SchoolUserUpdateRequest updateRequest) {
        if (updateRequest.id() == null) throw new RuntimeException("Id of update request cant be null");
        SchoolUserEntity userToUpdate = schoolUserRepository.findById(updateRequest.id())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        userToUpdate.setFirstname(updateRequest.firstname() != null ? updateRequest.firstname() : userToUpdate.getFirstname());
        userToUpdate.setLastname(updateRequest.lastname() != null ? updateRequest.lastname() : userToUpdate.getLastname());
        userToUpdate.setRole(updateRequest.role() != null ? updateRequest.role() : userToUpdate.getRole());
        return schoolUserMapper.toDomain(schoolUserRepository.save(userToUpdate));
    }

    //Ne koristi se
/*    public SchoolUserResponse updateUser(Integer id, SchoolUserEntity updateUser) {
        SchoolUserResponse response = SchoolUserResponse.builder().build();
        try {
            SchoolUserEntity user = schoolUserRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
            user.setUsername(updateUser.getUsername() == null ? user.getUsername() : updateUser.getUsername());
            user.setFirstname(updateUser.getFirstname() == null ? user.getFirstname() : updateUser.getFirstname());
            user.setLastname(updateUser.getLastname() == null ? user.getLastname() : updateUser.getLastname());
            user.setRole(updateUser.getRole() == null ? user.getRole() : updateUser.getRole());
            if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
                user.setPassword(authService.encodePassword(updateUser.getPassword()));
            }
            SchoolUserEntity savedUser = schoolUserRepository.save(user);
            response.setSchoolUser(schoolUserMapper.toDomain(savedUser));
            response.setStatusCode(200);
            response.setMessage("User updated successfully!");
        } catch (Exception exception) {
            response.setStatusCode(500);
            response.setError(exception.getMessage());
        }
        return response;
    }*/

    public SchoolUser getMyInfo(String username) {
        SchoolUserEntity user = schoolUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        return schoolUserMapper.toDomain(user);
    }
}
