package com.StreetAlert.Street_Alert.service;

import com.StreetAlert.Street_Alert.entity.Article;
import com.StreetAlert.Street_Alert.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
	private final ArticleRepository articleRepository;

	public List<Article> listArticles(Article.Sector sector, Article.ImpactLevel impact) {
		if (sector != null && impact != null) {
			return articleRepository.findBySectorAndImpactLevelOrderByPublishedAtDesc(sector, impact);
		}
		if (sector != null) {
			return articleRepository.findBySectorOrderByPublishedAtDesc(sector);
		}
		if (impact != null) {
			return articleRepository.findByImpactLevelOrderByPublishedAtDesc(impact);
		}

		return articleRepository.findAll(Sort.by(Sort.Direction.DESC, "publishedAt"));
	}
}
