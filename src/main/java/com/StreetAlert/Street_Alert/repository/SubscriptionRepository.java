package com.StreetAlert.Street_Alert.repository;

import com.StreetAlert.Street_Alert.entity.UserSubscription;
import com.StreetAlert.Street_Alert.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<UserSubscription, Long> {
	List<UserSubscription> findBySector(Article.Sector sector);

	List<UserSubscription> findByUserId(Long userId);

	Optional<UserSubscription> findByIdAndUserId(Long id, Long userId);

	Optional<UserSubscription> findByUserIdAndSector(Long userId, Article.Sector sector);
}
