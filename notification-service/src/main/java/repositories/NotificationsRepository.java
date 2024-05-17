package repositories;

import models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface NotificationsRepository extends JpaRepository<Notifications, UUID>{

}
