package com.StreetAlert.Street_Alert.repository;

import com.StreetAlert.Street_Alert.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notifications, Long> {
	List<Notifications> findByUserIdOrderByCreatedAtDesc(Long userId);

	long countByUserIdAndIsReadFalse(Long userId);

	Optional<Notifications> findByIdAndUserId(Long id, Long userId);
}
