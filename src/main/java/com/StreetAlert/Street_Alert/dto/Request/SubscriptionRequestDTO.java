package com.StreetAlert.Street_Alert.dto.Request;

import com.StreetAlert.Street_Alert.entity.Article;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequestDTO {
	@NotNull
	private Article.Sector sector;

	@NotNull
	private Article.ImpactLevel minImpact;
}
