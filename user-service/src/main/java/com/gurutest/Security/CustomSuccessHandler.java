//package com.gurutest.Security;
//
//import com.gurutest.Users.UserRepo;
//import com.gurutest.Users.UserService;
//import com.gurutest.Users.Users;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class CustomSuccessHandler implements AuthenticationSuccessHandler {
//    @Autowired
//    UserRepo userRepo;
//
//    @Autowired
//    DefaultOAuth2UserService userService;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//
//        String redirectUrl = null;
//        if(authentication.getPrincipal() instanceof DefaultOAuth2User) {
//            DefaultOAuth2User  userDetails = (DefaultOAuth2User ) authentication.getPrincipal();
//            String username = userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login")+"@gmail.com" ;
//            if(userRepo.findByEmail(username) == null) {
//                Users user = new Users();
//                user.setEmail(username);
//                user.setFullname(userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login"));
//                user.setPassword(("Dummy"));
//                user.setRole("FREELANCER");
//                userRepo.save(user);
//            }
//        }  redirectUrl = "/";
//        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
//    }
//}
