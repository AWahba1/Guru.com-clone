package com.gurutest.registeration.Password;

import com.gurutest.Users.UserRepo;
import com.gurutest.Users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService implements IPasswordResetTokenService {
    private final PasswordResetTokenRepo passwordResetTokenRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

//    public PasswordResetTokenService(PasswordResetTokenRepo passwordResetTokenRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
//        this.passwordResetTokenRepo = passwordResetTokenRepo;
//        this.userRepo = userRepo;
//        this.passwordEncoder = passwordEncoder;
//    }

    @Override
    public String validatePasswordResetToken(String theToken) {
        Optional<PasswordResetToken> passwordResetToken=passwordResetTokenRepo.findByToken(theToken);
        if(passwordResetToken.isEmpty()){
           return "Invalid";
        }
        Calendar calendar = Calendar.getInstance();
        if(passwordResetToken.get().getExpiryDate().getTime() -(calendar.getTime().getTime()) <=0){
            return "Expired";
        }
        return "valid";
    }

    @Override
    public Optional<Users> findUserByPasswordResetToken(String theToken) {
        return Optional.ofNullable(passwordResetTokenRepo.findByToken(theToken).get().getUser());
    }

    @Override
    public void resetPassword(Users theUser, String thePasswordResetToken) {
        theUser.setPassword(passwordEncoder.encode(thePasswordResetToken));
        userRepo.save(theUser);
        PasswordResetToken tokenEntity = passwordResetTokenRepo.findByUser(theUser);
        if (tokenEntity != null) {
            passwordResetTokenRepo.delete(tokenEntity);
        }
    }

    @Override
    public void createPasswordResetTokenForYser(Users user, String passwordResetToken) {
    PasswordResetToken passwordResetTokenEntity = new PasswordResetToken(passwordResetToken,user);
    passwordResetTokenRepo.save(passwordResetTokenEntity);
    }
}
