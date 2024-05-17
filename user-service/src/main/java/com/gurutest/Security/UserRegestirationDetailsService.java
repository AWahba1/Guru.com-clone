package com.gurutest.Security;

import com.gurutest.Users.UserRepo;
import com.gurutest.Users.Users;
import com.gurutest.registeration.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserRegestirationDetailsService implements UserDetailsService {
    private final UserRepo userRepository;

    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String email ) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserRegestirationDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")) ;
    }


}
