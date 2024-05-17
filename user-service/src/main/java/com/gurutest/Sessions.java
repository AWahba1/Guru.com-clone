package com.gurutest;
import com.gurutest.Users.Users;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Sessions {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @OneToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private Users user;

        private String token;
        private LocalDateTime expirationTime;

        public Sessions(Long id, Users user, String token, LocalDateTime expirationTime) {
                this.id = id;
                this.user = user;
                this.token = token;
                this.expirationTime = expirationTime;
        }

        public Sessions() {

        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Users getUser() {
                return user;
        }

        public void setUser(Users user) {
                this.user = user;
        }

        public String getToken() {
                return token;
        }

        public void setToken(String token) {
                this.token = token;
        }

        public LocalDateTime getExpirationTime() {
                return expirationTime;
        }

        public void setExpirationTime(LocalDateTime expirationTime) {
                this.expirationTime = expirationTime;
        }
}
