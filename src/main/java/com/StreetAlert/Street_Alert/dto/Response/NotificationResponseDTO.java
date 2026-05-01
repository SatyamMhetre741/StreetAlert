package com.StreetAlert.Street_Alert.dto.Response;

import com.StreetAlert.Street_Alert.entity.Article;
import com.StreetAlert.Street_Alert.entity.Notifications;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class NotificationResponseDTO {
	private Long id;
	private String message;
	private Article.Sector sector;
	private Article.ImpactLevel impact;
	private LocalDateTime createdAt;
	private boolean read;

	public static NotificationResponseDTO from(Notifications notification) {
		Article article = notification.getArticle();
		String title = article != null ? article.getTitle() : "New article";
		String sector = article != null && article.getSector() != null ? article.getSector().name() : "GENERAL";
		String message = "New " + sector + " update: " + title;

		return NotificationResponseDTO.builder()
				.id(notification.getId())
				.message(message)
				.sector(article != null ? article.getSector() : null)
				.impact(article != null ? article.getImpactLevel() : null)
				.createdAt(notification.getCreatedAt())
				.read(Boolean.TRUE.equals(notification.getIsRead()))
				.build();
	}
}
