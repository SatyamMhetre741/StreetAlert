package com.StreetAlert.Street_Alert.repository;

import com.StreetAlert.Street_Alert.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArticleRepository extends JpaRepository<Article, Long> {
}
