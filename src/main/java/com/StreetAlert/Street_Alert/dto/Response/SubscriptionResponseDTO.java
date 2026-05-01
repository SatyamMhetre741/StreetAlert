package com.StreetAlert.Street_Alert.dto.Response;

import com.StreetAlert.Street_Alert.entity.Article;
import com.StreetAlert.Street_Alert.entity.UserSubscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SubscriptionResponseDTO {
    private Long id;
    private Article.Sector sector;
    private Article.ImpactLevel minImpact;

    public static SubscriptionResponseDTO from(UserSubscription subscription) {
        return SubscriptionResponseDTO.builder()
                .id(subscription.getId())
                .sector(subscription.getSector())
                .minImpact(subscription.getMinImpactLevel())
                .build();
    }
}
