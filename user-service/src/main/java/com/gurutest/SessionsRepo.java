package com.gurutest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionsRepo extends JpaRepository<Sessions, Long> {
}
