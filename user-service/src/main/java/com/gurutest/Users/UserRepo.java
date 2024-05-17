package com.gurutest.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Integer> {
    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
    Optional<Users> findByFullname(String username);


    @Modifying
    @Query(value = "UPDATE Users u set u.fullname =:fullname,"+
              "u.email =:email where u.id =:id")
    void update(String fullname, String email, int id);
}
