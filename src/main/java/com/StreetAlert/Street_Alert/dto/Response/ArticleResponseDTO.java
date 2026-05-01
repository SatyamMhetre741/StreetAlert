package com.StreetAlert.Street_Alert.dto.Response;

import com.StreetAlert.Street_Alert.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ArticleResponseDTO {
	private Long id;
	private String title;
	private String description;
	private String url;
	private String source;
	private LocalDateTime publishedAt;
	private Article.Sector sector;
	private Article.ImpactLevel impact;

	public static ArticleResponseDTO from(Article article) {
		return ArticleResponseDTO.builder()
				.id(article.getId())
				.title(article.getTitle())
				.description(article.getDescription())
				.url(article.getUrl())
				.source(article.getSource())
				.publishedAt(article.getPublishedAt())
				.sector(article.getSector())
				.impact(article.getImpactLevel())
				.build();
	}
}
