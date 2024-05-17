package com.gurutest.Authentication;


import com.gurutest.Cahce.CacheRepository;
import com.gurutest.Cahce.CacheToken;
import com.gurutest.Users.UserRepo;
import com.gurutest.Users.Users;
import com.gurutest.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.DisabledException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

private final PasswordEncoder passwordEncoder;
private final UserRepo userRepository;
private final JwtService jwtService;
private final AuthenticationManager authenticationManager;

private final JwtUserDetailsService userDetailsService;

private final CacheRepository cachedTokenRepository;


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            System.out.println("Wrong credentials");
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    public String getToken(String userName, String password) throws Exception{
        authenticate(userName, password);
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(userName);
        final String token = jwtService.generateToken(userDetails);
        return token;

    }
    @Cacheable(value = "user", key = "#username")
    public String login(String username, String password) throws Exception {
        final String token = getToken(username,password);
        String userId = userDetailsService.tokenToId(token);
       // cacheAToken(token,userId);
    System.out.println(token +"USER TOKEN" + userId);
        return token;
    }

//    @CacheEvict(value="token", key = "#token")
//    public String logout(String token) throws Exception{
//        try{
//            verify(token);
//        } catch(Exception e){
//            throw e;
//        }
//        cachedTokenRepository.deleteCachedToken(token);
//        return "logged out successfully";
//    }
////
//    @CachePut(cacheNames = "token", key="#token")
//    public String verify(String token) throws Exception{
//        //Cache cache = cacheManager.getCache("token");
//        CacheToken cachedToken = cachedTokenRepository.findCachedTokenByToken(token);
//        if(cachedToken == null){
//            throw new Exception("token is not in cache");
//        }
//        String id=cachedToken.getUserId();
//        int userId = Integer.parseInt(id);
//       // Users user = userRepository.findById((cachedToken.getUserId()));
//        return cachedToken.getUserId();
//    }

//    public void cacheAToken(String token, String userId){
//        CacheToken t = new CacheToken();
//        t.setToken(token);
//        t.setUserId(userId);
//        System.out.println(t.getUserId() + t.getToken());
//        cachedTokenRepository.save(t);
//    }


//------------------------------------------------------DOWNWARDS is OLD


//    public JwtResponse register(RegisterRequest registerRequest) {
//        String fullName = registerRequest.getFullName();
//        String email = registerRequest.getEmail();
//        String password = passwordEncoder.encode(registerRequest.getPassword());
//        String role = "ROLE_USER";
//        var user = new Users(fullName, email,password,role);
//        userRepository.save(user);
//        var jwtToken= jwtService.generateToken(user);
//        return JwtResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//    public JwtResponse authenticate(JwtRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//        var user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow();
//        var jwtToken = jwtService.generateToken((UserDetails) user);
//        return JwtResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//    private void saveUserToken(Users user, String jwtToken) {
//        var token = Token.builder()
//                .user(user)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepository.save(token);
//    }

//    private void revokeAllUserTokens(User user) {
//        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//        if (validUserTokens.isEmpty())
//            return;
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
//    }

//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(refreshToken);
//        if (userEmail != null) {
//            var user = this.repository.findByEmail(userEmail)
//                    .orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }
}
