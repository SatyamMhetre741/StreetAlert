package com.StreetAlert.Street_Alert.repository;

import com.StreetAlert.Street_Alert.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {
}
