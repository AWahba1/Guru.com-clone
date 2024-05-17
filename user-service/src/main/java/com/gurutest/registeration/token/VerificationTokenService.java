package com.gurutest.registeration.token;

import com.gurutest.Users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor

public class VerificationTokenService implements IVerificationTokenService {

    private final VerificationTokenRepo verificationTokenRepo;
    @Override
    public String validateToken(String token) {
        return "";
    }

    @Override
    public void saveVerificationTokenForUser(Users user, String token) {

    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public void deleteUserToken(int id) {
        verificationTokenRepo.deleteByUserId(id);
    }
}
