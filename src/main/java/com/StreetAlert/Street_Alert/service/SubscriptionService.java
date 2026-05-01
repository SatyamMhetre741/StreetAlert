package com.StreetAlert.Street_Alert.service;

import com.StreetAlert.Street_Alert.dto.Request.SubscriptionRequestDTO;
import com.StreetAlert.Street_Alert.entity.User;
import com.StreetAlert.Street_Alert.entity.UserSubscription;
import com.StreetAlert.Street_Alert.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
	private final SubscriptionRepository subscriptionRepository;

	public List<UserSubscription> listForUser(Long userId) {
		return subscriptionRepository.findByUserId(userId);
	}

	public UserSubscription createForUser(User user, SubscriptionRequestDTO request) {
		subscriptionRepository.findByUserIdAndSector(user.getId(), request.getSector())
				.ifPresent(existing -> {
					throw new IllegalArgumentException("Subscription already exists for sector: " + request.getSector());
				});

		UserSubscription subscription = UserSubscription.builder()
				.user(user)
				.sector(request.getSector())
				.minImpactLevel(request.getMinImpact())
				.build();

		return subscriptionRepository.save(subscription);
	}

	public UserSubscription updateForUser(Long userId, Long subscriptionId, SubscriptionRequestDTO request) {
		UserSubscription subscription = subscriptionRepository.findByIdAndUserId(subscriptionId, userId)
				.orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

		if (request.getSector() != null) {
			subscription.setSector(request.getSector());
		}
		if (request.getMinImpact() != null) {
			subscription.setMinImpactLevel(request.getMinImpact());
		}

		return subscriptionRepository.save(subscription);
	}

	public void deleteForUser(Long userId, Long subscriptionId) {
		UserSubscription subscription = subscriptionRepository.findByIdAndUserId(subscriptionId, userId)
				.orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
		subscriptionRepository.delete(subscription);
	}
}
