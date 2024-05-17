package com.gurutest.OAuth;

import com.gurutest.Users.UserRepo;
import com.gurutest.Users.Users;
import com.gurutest.registeration.token.VerificationToken;
import com.gurutest.registeration.token.VerificationTokenRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Autowired
    UserRepo userRepo;
    @Autowired
    VerificationTokenRepo verificationTokenRepo;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//@Autowired
//private DefaultUserService userService;

    public Optional<Users> getByEmail(String email) {
        // return userRepo.findByEmail(email);
        return Optional.ofNullable(userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }
    private boolean isUserExists(String email) {
        try {
            Optional<Users> user=  this.getByEmail(email);
        }
        catch (UsernameNotFoundException e) {
            return false;
        }
        return true;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication
            //,@RequestHeader ("Authorization") String token
                                        )
                throws ServletException, IOException {
        String redirectUrl = null;

        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
           // String autho=userDetails.getAttribute("authorities").toString();
            String username = userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login")+"@gmail.com" ;
            String fullname= userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login");
            String role=userDetails.getAttribute("role")!=null?userDetails.getAttribute("role"):userDetails.getAttribute("login");
            String password= userDetails.getAttribute("password") !=null?userDetails.getAttribute("password"):"";
            System.out.println("OAuth2 user name: " + username);
            System.out.println("OAuth2 full name: " + fullname);



            // i should check if this user is already there or not LAZMMMM
                if(!isUserExists(username)) {
                    var user = new Users();
                    user.setEmail(username);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setFullname(fullname);
                    user.setRole("FREELANCER");

                    user.setEnabled(true);
//                Collection<? extends GrantedAuthority> auth= Arrays.stream(user.getRole()
//                                .split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());

                    userRepo.save(user);
                    // Create verification token
                    String verificationToken = UUID.randomUUID().toString();
                    VerificationToken token = new VerificationToken( user,verificationToken);
                    verificationTokenRepo.save(token);
                    System.out.println("Verification token created: " + verificationToken);

                    // Set the verification URL and redirect
                   // String verificationUrl = request.getRequestURL().toString().replace("/login", "/verifyEmail?token=" + verificationToken);
                  //  System.out.println("Verification URL: " + verificationUrl);
                    System.out.println("Saved user-->>: " + user.getFullname());
                    //System.out.println("auth-->>: " + auth);
                }
                else{
                    System.out.println("OAuth2 full name: "+fullname );
                    System.out.println("User ALready Exist");


                }
            redirectUrl = "/";
        }


        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);


//         else {
//            // Handle unexpected principal type
//            System.out.println("User Already Exist");
//           // System.out.println("Unexpected principal type: " + authentication.getPrincipal().getClass());
//        }

        super.onAuthenticationSuccess(request, response, authentication);


    }
}
