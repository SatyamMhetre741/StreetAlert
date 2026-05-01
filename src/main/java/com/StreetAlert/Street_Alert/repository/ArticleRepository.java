package com.StreetAlert.Street_Alert.repository;

import com.StreetAlert.Street_Alert.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// repository/ArticleRepository.java
public interface ArticleRepository extends JpaRepository<Article, Long> {

    boolean existsByUrlHash(String urlHash);

    List<Article> findByStatus(Article.ArticleStatus status);

    List<Article> findBySectorAndImpactLevel(Article.Sector sector, Article.ImpactLevel impactLevel);

    List<Article> findBySectorOrderByPublishedAtDesc(Article.Sector sector);

    List<Article> findByImpactLevelOrderByPublishedAtDesc(Article.ImpactLevel impactLevel);

    List<Article> findBySectorAndImpactLevelOrderByPublishedAtDesc(Article.Sector sector, Article.ImpactLevel impactLevel);
}