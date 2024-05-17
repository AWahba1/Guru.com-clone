package com.gurutest.Users;

import com.gurutest.registeration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<Users> getUsers();
    Users RegisterUser(RegistrationRequest request);
    Optional<Users> findByEmail  (String email);
    Optional<Users> findByFullname(String username);

    String saveUserVerificationToken(Users theUser, String verToken);

    String validateToken(String verificationToken);
    Optional<Users> findById(int id);

    void updateUser(int id, String fullname, String email);

    String deleteUser(int id);
}
