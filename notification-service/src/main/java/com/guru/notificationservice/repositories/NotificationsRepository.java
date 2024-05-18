package com.guru.notificationservice.repositories;

import com.guru.notificationservice.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface NotificationsRepository extends JpaRepository<Notifications, UUID>{

    @Query(value = "SELECT n FROM Notifications n WHERE n.userId =(?1)",nativeQuery = true)
    List<Notifications> findAllNotificationsByUserId(UUID userId);

    @Query(value = "UPDATE Notifications n SET n.isRead = true WHERE n.id =(?1)",nativeQuery = true)
    void markNotificationAsRead(UUID id);


}
