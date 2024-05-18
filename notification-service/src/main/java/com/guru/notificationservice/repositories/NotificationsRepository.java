package com.guru.notificationservice.repositories;

import com.guru.notificationservice.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.UUID;


public interface NotificationsRepository extends JpaRepository<Notification, UUID>{

//    @Query(value = "INSERT INTO notifications (user_id,message) values (?1,?2)",nativeQuery = true)
//    void createNewNotification(UUID userId, String message);

    @Query(value = "SELECT * FROM notifications n WHERE n.user_id =(?1)",nativeQuery = true)
    List<Notification> findAllNotificationsByUserId(UUID user_id);


    @Procedure(procedureName = "mark_as_read")
    void markNotificationAsRead(UUID _notification_id);


}
