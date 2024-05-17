package com.gurutest.registeration.token;

import com.gurutest.Users.Users;

import java.util.Optional;

public interface IVerificationTokenService {
    String validateToken(String token);
    void saveVerificationTokenForUser(Users user, String token);
    Optional<VerificationToken> findByToken(String token);


    void deleteUserToken(int id);
}
