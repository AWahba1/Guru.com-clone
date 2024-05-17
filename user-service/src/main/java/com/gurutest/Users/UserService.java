package com.gurutest.Users;

import com.gurutest.registeration.RegistrationRequest;
import com.gurutest.registeration.token.VerificationToken;
import com.gurutest.registeration.token.VerificationTokenRepo;
import com.gurutest.registeration.token.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepo verificationTokenRepo;

  private final VerificationTokenService verificationTokenService ;
    public boolean isValidPassword(String password) {
        // Add your password rules here
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }

    @Override
    public List<Users> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public Users RegisterUser(RegistrationRequest request) {
        var user = new Users(request.getFullname(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole());

        return userRepo.save(user);


//       // System.out.println("Registering user"+request.getEmail()+" "+request.getPassword()+isValidPassword(request.getPassword()));
//        Optional<Users> userE=this.findByEmail(request.getEmail());
//        try {
//            if (userE.isPresent()) {
//                System.out.println("User already exists");
//                throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
//            }
//
//                // Email is available, check password validity
//            else if (!isValidPassword(request.getPassword())) {
//                    System.out.println("Invalid password: " + isValidPassword(request.getPassword()));
//                    throw new IllegalArgumentException("Password must contain at least 8 characters, lowercase, uppercase, and special characters: !,@,#,$,*,^,&,*,(");
//                }
//                else {
//                    // Register the user
//                    var user = new Users(request.getFullname(), request.getEmail(),
//                            passwordEncoder.encode(request.getPassword()),
//                            request.getRole());
//                    return userRepo.save(user);
//                }
//        }
//        catch (UserAlreadyExistsException ex) {
//            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
//        } catch (IllegalArgumentException ex) {
//            // Return a custom message when the password is invalid
//            throw new IllegalArgumentException("Invalid password: " + ex.getMessage());
//        }

//        try {
//            if (userE.isPresent()) {
//                System.out.println("User already exists");
//                throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
//            }
//            try {
//                System.out.println(isValidPassword(request.getPassword()));
//                if (!isValidPassword(request.getPassword())) {
//                    System.out.println("Invalid password"+ isValidPassword(request.getPassword()));
//                    throw new IllegalArgumentException("Password must contains at least 8 characters, lowercase, uppercase and special characters: !,@,#,$,*,^,&,*,(,)");
//                }
//                var user = new Users(request.getFullname(), request.getEmail(),
//                        passwordEncoder.encode(request.getPassword()),
//                        request.getRole());
//                return userRepo.save(user);
//            }
//            catch (IllegalArgumentException ex) {
//                // Return a custom message when the password is invalid
//                throw new IllegalArgumentException("Invalid password: " + ex.getMessage());
//            }
//        }
//        catch (UserAlreadyExistsException ex) {
//            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
//        }
    }

    @Override
    public Optional<Users> findByEmail(String email) {
       // return userRepo.findByEmail(email);
        return Optional.ofNullable(userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public Optional<Users> findByFullname(String username) {
        return Optional.ofNullable(userRepo.findByFullname(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public Optional<Users> findById(int id) {
        return userRepo.findById(id);
    }
   // @Transactional
    @Override
    public void updateUser(int id, String fullname, String email) {
        userRepo.update(fullname, email, id);
    }

    @Transactional
    @Override
    public String deleteUser(int id) {
        //kant public void msh public String ashan lw bazet
        Optional<Users> theUser = userRepo.findById(id);
        System.out.println(theUser +">>>>>>>>>" +theUser.isPresent());
        theUser.ifPresent(user -> verificationTokenService.deleteUserToken((user.getId())));
        userRepo.deleteById(id);
        return "User got DELETEDDDDD";
    }
    @Override
    public String saveUserVerificationToken(Users theUser, String verToken) {
        var verificationToken = new VerificationToken(theUser,verToken);
        verificationTokenRepo.save(verificationToken);
        return "saveToken" + verToken;
    }

    @Override
    public String validateToken(String verificationToken) {
        VerificationToken verificationToken1 = verificationTokenRepo.findByToken(verificationToken);
        if(verificationToken1==null){
            return "Invalid verification token";
        }
        Users user= verificationToken1.getUser();
        Calendar calendar = Calendar.getInstance();
//        if(verificationToken1.getExpiryDate().after(calendar.getTime())){
//            verificationTokenRepo.delete(verificationToken1);
//            return "Expired verification token";
//        }
        //That's the way in the video
        if((verificationToken1.getExpiryDate().getTime() -(calendar.getTime().getTime()))<=0){
           verificationTokenRepo.delete(verificationToken1);
            return "Expired";
        }
        user.setEnabled(true);
        userRepo.save(user);
        return "valid";
    }



//    public void updateEmail(String currentEmail, String newEmail) {
//        // Get the user entity from the repository using the current email
//        Optional<Users> optionalUser = userRepo.findByEmail(currentEmail);
//        if (optionalUser.isPresent()) {
//            Users user = optionalUser.get();
//            // Update the user's email address
//            user.setEmail(newEmail);
//            userRepo.save(user); // Save the updated user entity
//        } else {
//            // Handle case where user with current email is not found
//            throw new EntityNotFoundException("User with email " + currentEmail + " not found");
//        }
//    }
}









//package com.gurutest.Users;
//
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Service
//public record UserService(UserRepo userRepo) {
//
//    public void registerUser(UserRegisterRequest request){
//
//        Users user = Users.builder()
//                .fullname(request.fullname())
//                .email(request.email())
//                .password(request.password())
//                .role(request.role())
//                .status(UserStatus.PENDING_VERIFICATION)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        //check if mail is taken
//        //check if mail is valid
//
//        // Check if email is valid (you can use JavaMail or a validation library)
//        if (!isValidEmail(request.email())) {
//            throw new IllegalArgumentException("Invalid email format");
//        }
//
//        // Check if email is already registered
//        if (userRepo.existsByEmail(request.email())) {
//            throw new IllegalArgumentException("Email is already registered");
//        }
//        //save user in db
//        userRepo.save(user);
//
//    }
//    private boolean isValidEmail(String email) {
//        // Regular expression pattern for basic email validation
//        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//
//        // Compile the regex pattern
//        Pattern pattern = Pattern.compile(emailRegex);
//
//        // Match the email against the pattern
//        Matcher matcher = pattern.matcher(email);
//
//        // Return true if the email matches the pattern (valid), false otherwise
//        return matcher.matches();
//    }
//}
