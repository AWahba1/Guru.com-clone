package com.gurutest.Users;
import com.gurutest.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    @Autowired
    private JwtService jwtService;
    private final IUserService userService;
    @Autowired
    private UserRepo userRepository;
//    @GetMapping
//    public List<Users> getUsers(){
//        return userService.getUsers() ;
//    }
    @GetMapping("/token")
    public ResponseEntity<?> getToken(@RequestHeader ("Authorization") String token) throws Exception {
        token = token.substring(7);
    System.out.println("HOOOO"+"token"+token);
    return ResponseEntity.ok( token);
    }

    @GetMapping
    public String getUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            model.addAttribute("users", userService.findByEmail(username).get());
            Optional<Users> users=  userService.findByEmail(username);
            System.out.println( users.get().getFullname());
        }
        catch (UsernameNotFoundException e) {
            e.printStackTrace();
        }


        return "users";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model){
        Optional<Users> user = userService.findById(id);
        model.addAttribute("user", user.get());
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, Users user){
        userService.updateUser(id, user.getFullname(), user.getEmail());
        return "redirect:/users?update_success";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id , HttpServletRequest request, HttpServletResponse response){
        userService.deleteUser(id);
        // Check if the user is authenticated and logout if they are
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/users?delete_success";
    }



//    @PutMapping("/{userId}/fullname")
//    public ResponseEntity<String> updateUsername(@PathVariable int userId, @RequestBody String newFullname) {
//        try {
//            userService.updateUsername(userId, newFullname);
//            return ResponseEntity.ok("Full name updated successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update full name");
//        }
//    }

//    @PutMapping("/{userId}/email")
//    public ResponseEntity<String> updateEmail(@PathVariable int userId, @RequestBody String newEmail) {
//        try {
//            userService.updateEmail(userId, newEmail);
//            return ResponseEntity.ok("Email updated successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update email");
//        }
//    }


//    @PutMapping("/updateEmail")
//    public ResponseEntity<String> updateEmail(@AuthenticationPrincipal UserDetails userDetails, @RequestBody String newEmail) {
//        // Get the authenticated user's username or email from UserDetails
//        String currentEmail = userDetails.getUsername();
//        // Update the email address in your database or user repository
//        userService.updateEmail(currentEmail, newEmail);
//
//        return ResponseEntity.ok("Email updated successfully");
//    }

}






//package com.gurutest.Users;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("api/v1/signup")
//public record UserController(com.gurutest.Users.UserService UserService) {
//
//    @PostMapping
//    public void registerUser(@RequestBody UserRegisterRequest request) {
//        log.info("register",request);
//        UserService.registerUser(request);
//    }
//}
