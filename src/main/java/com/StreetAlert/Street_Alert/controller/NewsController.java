package com.StreetAlert.Street_Alert.controller;

import com.StreetAlert.Street_Alert.dto.Response.ArticleResponseDTO;
import com.StreetAlert.Street_Alert.entity.Article;
import com.StreetAlert.Street_Alert.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
	private final NewsService newsService;

	@GetMapping
	public ResponseEntity<List<ArticleResponseDTO>> list(
			@RequestParam(required = false) Article.Sector sector,
			@RequestParam(required = false) Article.ImpactLevel impact) {
		List<ArticleResponseDTO> response = newsService.listArticles(sector, impact).stream()
				.map(ArticleResponseDTO::from)
				.toList();
		return ResponseEntity.ok(response);
	}
}
