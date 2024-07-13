package rs.ac.bg.etf.osrpavicevic.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.respository.SchoolUserRepository;

@Service
@RequiredArgsConstructor
public class SchoolUserService implements UserDetailsService {

    private final SchoolUserRepository schoolUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return schoolUserRepository.findByUsername(username).orElseThrow();
    }
}
