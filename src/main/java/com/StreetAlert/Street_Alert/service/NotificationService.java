package com.StreetAlert.Street_Alert.service;

import com.StreetAlert.Street_Alert.entity.Article;
import com.StreetAlert.Street_Alert.entity.Notifications;
import com.StreetAlert.Street_Alert.entity.UserSubscription;
import com.StreetAlert.Street_Alert.repository.NotificationRepository;
import com.StreetAlert.Street_Alert.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
	private final SubscriptionRepository subscriptionRepository;
	private final NotificationRepository notificationRepository;

	public void dispatchNotifications(Article article) {
		if (article == null || article.getSector() == null || article.getImpactLevel() == null) {
			return;
		}

		List<UserSubscription> subscriptions = subscriptionRepository.findBySector(article.getSector());
		if (subscriptions.isEmpty()) {
			return;
		}

		for (UserSubscription subscription : subscriptions) {
			if (!isImpactEligible(article.getImpactLevel(), subscription.getMinImpactLevel())) {
				continue;
			}

			Notifications notification = Notifications.builder()
					.user(subscription.getUser())
					.article(article)
					.isRead(false)
					.createdAt(LocalDateTime.now())
					.build();

			notificationRepository.save(notification);
		}

		log.info("Dispatched notifications for article [{}]", article.getId());
	}

	public List<Notifications> listForUser(Long userId) {
		return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}

	public long unreadCount(Long userId) {
		return notificationRepository.countByUserIdAndIsReadFalse(userId);
	}

	public void markAsRead(Long userId, Long notificationId) {
		Notifications notification = notificationRepository.findByIdAndUserId(notificationId, userId)
				.orElseThrow(() -> new IllegalArgumentException("Notification not found"));
		notification.setIsRead(true);
		notificationRepository.save(notification);
	}

	private boolean isImpactEligible(Article.ImpactLevel articleImpact, Article.ImpactLevel minImpact) {
		if (minImpact == null) return true;
		return articleImpact.ordinal() <= minImpact.ordinal();
	}
}
