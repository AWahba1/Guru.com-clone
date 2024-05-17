package com.gurutest.Users;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.Objects;
@Entity
@Getter
@Setter
@NoArgsConstructor

public class Users {
    @Id
    @SequenceGenerator(
            name="user_id_sequence",
            sequenceName ="user_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    private Integer id;
    private String fullname;
    private String password;
    @NaturalId(mutable = true)
    private String email;
   // @Enumerated(EnumType.STRING)
//    private UserRole role;
    private String role;
    private boolean isEnabled;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    public static boolean isUserEnabled(UserStatus status) {
        return status == UserStatus.VERIFIED;
    }
    public Users(String fullname, String email,
                String password, String role) {

        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = role;
    }




}
