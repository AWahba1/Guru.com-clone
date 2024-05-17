package com.gurutest.registeration;

import com.gurutest.Users.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

//
//public record RegistrationRequest(String fullname,
//                                  String email,
//                                  String password,
//                                  String role) {
//
////    public RegistrationRequest() {
////        this("", "", "", "");
////    }
//}

@Data
public class RegistrationRequest {
    private String fullname;
    private String password;
    private String role;
    private String email;
}
