package com.guru.notificationservice.repositories;

import com.guru.notificationservice.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface NotificationsRepository extends JpaRepository<Notifications, UUID>{

}
