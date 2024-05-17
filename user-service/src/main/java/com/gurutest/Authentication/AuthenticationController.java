package com.gurutest.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class AuthenticationController {
    @Autowired
    AuthenticationService authService;

    @Autowired
    JwtUserDetailsService userDetailsService;

    private String username;

//    @PostMapping("/login")
//    public String login(@RequestBody JwtRequest authenticationRequest) throws Exception {
//        final String token = authService.login(authenticationRequest.getEmail(),authenticationRequest.getPassword());
//        return "login";
//    }

}
