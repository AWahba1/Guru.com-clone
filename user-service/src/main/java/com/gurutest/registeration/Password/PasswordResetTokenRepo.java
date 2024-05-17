package com.gurutest.registeration.Password;

import com.gurutest.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepo  extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String theToken);

    PasswordResetToken findByUser(Users theUser);
}
