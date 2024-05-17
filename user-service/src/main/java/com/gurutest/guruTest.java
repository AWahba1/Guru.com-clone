package com.gurutest;

import com.gurutest.Users.UserRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
//@RestController
//@RequestMapping("api/v1/getusers")
@EnableCaching
public class guruTest {
    private final UserRepo userrepo;

    public guruTest(UserRepo userrepo) {
        this.userrepo = userrepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(guruTest.class, args);
    }

}
