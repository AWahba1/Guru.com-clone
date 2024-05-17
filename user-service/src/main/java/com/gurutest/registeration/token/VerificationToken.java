package com.gurutest.registeration.token;

import com.gurutest.Users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;
    private Date expiryDate;
    private static final int EXPIRY=10;
    @OneToOne
    @JoinColumn(name="user_id")
    private Users user;

    public VerificationToken(Users user, String token) {
      super();
        this.user = user;
        this.token = token;
        this.expiryDate = this.getTokenExpiryDate();
    }
    public VerificationToken( String token) {
       super();
        this.token = token;
        this.expiryDate = this.getTokenExpiryDate();
    }

    public static Date getTokenExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRY);
        return new Date(calendar.getTime().getTime());

    }
}
