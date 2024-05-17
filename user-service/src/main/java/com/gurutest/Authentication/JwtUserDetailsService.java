package com.gurutest.Authentication;

import com.gurutest.Users.UserRepo;
import com.gurutest.Users.Users;
import com.gurutest.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;
@Autowired
    private JwtService jwtService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("There is no user with username: "+ username);
        }
        ArrayList<? extends GrantedAuthority> record = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(),
                record);
    }
    public String tokenToId(String token) {
        String username = jwtService.extractUsername(token);
       // String email = jwtService.extractEmail(token);
        System.out.println("Extracted Username from Token: " + username );
      Optional<Users> user = userRepository.findByEmail(username); // i need to find by username not email
       // Optional<Users> user = userRepository.findByFullname(username);
       // Users user = userRepository.findByFullname(username);
      //  System.out.println("Extracted User: " + user.get().getId() );

        if (user.isPresent()) {
            String userId = String.valueOf(user.get().getId());
            System.out.println("User ID: " + userId);
            return userId;
        }
        else {
            System.out.println("User not found for username: " + username);
            return null; // Or handle the case where user is not found
        }
      //  return String.valueOf(user.get().getId());
    }
    public Users save(Users user) throws IllegalAccessException,InstantiationException{
        Users newUser = Users.class.newInstance();
        newUser.setFullname(user.getFullname());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        return userRepository.save(newUser);
    }
}
