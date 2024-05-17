package com.gurutest.Home;

import com.gurutest.Authentication.AuthenticationService;
import com.gurutest.Authentication.JwtRequest;
import com.gurutest.Authentication.JwtResponse;
import com.gurutest.Users.UserRepo;
import com.gurutest.Users.Users;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:8080")
public class HomeController {
private final UserRepo userRepo;
private final AuthenticationService authService;

    @GetMapping()
    public String homePage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean auth = authentication.isAuthenticated();
        String email = authentication.getPrincipal().toString();
        System.out.println(username);
        System.out.println(auth);
        System.out.println(email);
        OAuth2User  user;
        model.addAttribute("auth", auth);
        return "home";
    }
    //da for front end but the header is null ashan jwt
    @GetMapping("/login")
    public String login() {
        //final String token = authService.login(authenticationRequest.getEmail(),authenticationRequest.getPassword());
        //System.out.println("USER TOKEN : "+token);
        return "login";
    }

    //da for  postman and it's working
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {
        System.out.println(authenticationRequest.getEmail()+"<<<<<<<<");
    final String token = authService.login(authenticationRequest.getEmail(),authenticationRequest.getPassword());
    System.out.println("USER TOKEN : "+token);
    return ResponseEntity.ok(new JwtResponse(token));
}
//    @PostMapping(value = "/logout")
//    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorization) throws Exception {
//        authorization = authorization.substring(7);
//        System.out.println(authorization+"LLLLLL");
//        return ResponseEntity.ok(authService.logout(authorization));
//    }
    @GetMapping("/error")
    public String error(){
        return "error";
    }
}
