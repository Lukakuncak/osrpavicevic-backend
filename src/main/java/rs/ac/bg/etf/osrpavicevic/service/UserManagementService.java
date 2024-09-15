package rs.ac.bg.etf.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.api.request.SchoolUserUpdateRequest;
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

    public SchoolUser getMyInfo(String username) {
        SchoolUserEntity user = schoolUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        return schoolUserMapper.toDomain(user);
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        authService.authenticatePasswordAndUsername(username,oldPassword);
        SchoolUserEntity user = schoolUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        user.setPassword(authService.encodePassword(newPassword));
        schoolUserRepository.save(user);
    }
}
