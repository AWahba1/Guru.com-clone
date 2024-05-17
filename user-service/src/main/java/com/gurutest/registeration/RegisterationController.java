package com.gurutest.registeration;

import com.gurutest.Event.Listener.RegistrationCompleteEventListener;
import com.gurutest.Event.RegistrationCompleteEvent;
import com.gurutest.Exceptions.UserAlreadyExistsException;
import com.gurutest.Users.IUserService;
import com.gurutest.Users.UserService;
import com.gurutest.Users.Users;
import com.gurutest.Utility.UrlUtil;
import com.gurutest.registeration.Password.IPasswordResetTokenService;
import com.gurutest.registeration.Password.PasswordResetTokenService;
import com.gurutest.registeration.token.VerificationToken;
import com.gurutest.registeration.token.VerificationTokenRepo;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class RegisterationController {
    private final IUserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenRepo verificationTokenRepo;
     private final IPasswordResetTokenService passwordResetTokenService;
     private final RegistrationCompleteEventListener registrationCompleteEventListener;

     @Autowired
    private  AmqpTemplate amqpTemplate;

     @GetMapping("/signup-form")
     public String signupForm(Model model) {
         model.addAttribute("user",new RegistrationRequest());
            return "registration";
     }

    // Helper method to check if a user already exists
    private boolean isUserExists(String email) {
         try {
           Optional<Users> user=  userService.findByEmail(email);
         }
         catch (UsernameNotFoundException e) {
             return false;
         }
        return true;
    }

    // Helper method to validate password
    private boolean isValidPassword(String password) {
        // Add your password rules here
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
     //i need to check if password is not valid then show error message and if user is already exist show the message
    //also role should be chosen between employer and freelancer
    //// @RequestBody lw 3ayza a-test in postman
    @PostMapping("/signup-request")
    public String registerUser(
            @ModelAttribute("user") RegistrationRequest registrationRequest,  HttpServletRequest request) {
        System.out.println("Received Request: " + registrationRequest);
        String redirect="";
        // Validate the password
        if (!isValidPassword(registrationRequest.getPassword())) {
            System.out.println("Invalid password");
            System.out.println(isValidPassword(registrationRequest.getPassword()));
            return "redirect:/signup/signup-form?errorPassword";
        }

        if (isUserExists(registrationRequest.getEmail())) {
            System.out.println("Email already exists");
            return "redirect:/signup/signup-form?errorUser";
        }
        else {
            System.out.println("Registering user-->>>>>>>>>");
            // Register the user
            Users user = userService.RegisterUser(registrationRequest);

            HashMap<String, Object> message = new HashMap<>();
            message.put("email", registrationRequest.getEmail());
            message.put("task", "sendVerifyEmail"); // Example task
            // Send the message to RabbitMQ queue
            amqpTemplate.convertAndSend("user_req", message
                 //,   m -> {
                       // m.getMessageProperties().setHeader("command", "VerifyCommand");
            );

            System.out.println("Successfully registered user: " + user.getEmail());
            eventPublisher.publishEvent(new RegistrationCompleteEvent(user, UrlUtil.getApplicationUrl(request)));
            return "redirect:/signup/signup-form?success";
        }

    }
        //old way
         //    try {
//        Users user = userService.RegisterUser(registrationRequest);
//        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationURL(request)));
//       // return "Registered Successfully, Please check your email to confirm";
//      System.out.println("Successfully registered user"+user.getEmail());
//        return "redirect:/signup/signup-form?success";
//    }
//    catch (UserAlreadyExistsException ex ) {
//      //  return "User already exists";
//        System.out.println("User already exists");
//        return "redirect:/signup//signup-form?error";
//    }
//    catch (IllegalArgumentException ex) {
//        System.out.println("incorrect password");
//        //return "Password must contain at least 8 characters, lowercase, uppercase and special characters: !,@,#,$,*,^,&,*,(,)";
//    return "redirect:/signup/signup-form?errorPassword";
//    }




@GetMapping("/verifyEmail")
    public String  verifyEmail(@RequestParam("token") String token){
    VerificationToken verificationToken= verificationTokenRepo.findByToken(token);
    if(verificationToken==null){
       // return "Invalid token";
        return "redirect:/error?invalid";
    }
    if(verificationToken.getUser().isEnabled()){
      // return "This Account is already verified, Please Log in";
        return "redirect:/login?verified";
    }
    String verificationResult=userService.validateToken(token);
    if(verificationResult.equalsIgnoreCase("valid")){

      //  return "Email verified successfully, Now you can log in to your account";
        return "redirect:/login?valid";
    }
    else if(verificationResult.equalsIgnoreCase("Expired")){
        return "redirect:/login?expired";
    }
    else {
        return "redirect:/error?invalid";
    }

    }

@GetMapping("/forgot-password-request")
    public String forgotPassword(){
        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String resetPasswordRequest(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        try {
            Optional<Users> user = userService.findByEmail(email);
            if(user.isEmpty()){
                System.out.println("Email not found");
                return  "redirect:/signup/forgot-password-request?not_found";
            }
            String PasswordResetToken= UUID.randomUUID().toString();
            passwordResetTokenService.createPasswordResetTokenForYser(user.get(),PasswordResetToken);
//send password reset verification email to the user
            String url = UrlUtil.getApplicationUrl(request)+"/signup/password-reset-form?token="+PasswordResetToken;
            try {
                System.out.println(user.get().getFullname());
                registrationCompleteEventListener.sendVerificationPassword(url,user);

                //eventPublisher.publishEvent(new RegistrationCompleteEvent(user.get(), UrlUtil.getApplicationUrl(request)));
            } catch (MessagingException | UnsupportedEncodingException e) {
                model.addAttribute("error", e.getMessage());
                throw new RuntimeException(e);
            }
            // return "Forgot Password is done";
            return "redirect:/signup/forgot-password-request?success";
        }
        catch (UsernameNotFoundException e) {
            return  "redirect:/signup/forgot-password-request?not_found";
        }

    }

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model){
        model.addAttribute("token", token);
        return "password-reset-form";
    }

    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request){
    String theToken = request.getParameter("token");
    String password = request.getParameter("password");
    String TokenVerificatinResult=passwordResetTokenService.validatePasswordResetToken(theToken);
    if(!TokenVerificatinResult.equalsIgnoreCase("valid")){
       // return "ERROR: Password cannot be verified ";
        return "redirect:/error?invalid_token";
    }
    Optional<Users> TheUser = passwordResetTokenService.findUserByPasswordResetToken(theToken);
    if(TheUser.isPresent()){
        passwordResetTokenService.resetPassword(TheUser.get(),password);
        //return "Password reset successfully";
        // i want to remove the token from the table so i can forgot password more than once

        return "redirect:/login?reset_success";
    }
   // return "ERROR: Not Found";
        return "redirect:/error?not_found";
    }

    public String applicationURL(HttpServletRequest request) {
         return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
                // request.getRequestURL().toString();
    }
}


