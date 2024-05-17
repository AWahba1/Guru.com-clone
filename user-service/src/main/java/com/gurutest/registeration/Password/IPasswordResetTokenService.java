package com.gurutest.registeration.Password;

import com.gurutest.Users.Users;

import java.util.Optional;

public interface IPasswordResetTokenService {
    String validatePasswordResetToken(String theToken);

    Optional<Users> findUserByPasswordResetToken(String theToken);
    void resetPassword(Users theUser,String thePasswordResetToken);

    void createPasswordResetTokenForYser(Users user, String passwordResetToken);
}
