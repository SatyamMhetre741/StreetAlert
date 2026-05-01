package com.StreetAlert.Street_Alert.scheduler;

import com.StreetAlert.Street_Alert.entity.Article;
import com.StreetAlert.Street_Alert.repository.ArticleRepository;
import com.StreetAlert.Street_Alert.service.NewsProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

// scheduler/ArticleProcessorJob.java
@Component
@Slf4j
public class ArticleProcessorJob {

    private final ArticleRepository articleRepository;
    private final NewsProcessingService processingService;

    public ArticleProcessorJob(ArticleRepository articleRepository,
                               NewsProcessingService processingService) {
        this.articleRepository = articleRepository;
        this.processingService = processingService;
    }

    @Scheduled(fixedDelay = 600000) // every 10 minutes
    public void retryUnprocessed() {
        List<Article> unprocessed = articleRepository.findByStatus(
                Article.ArticleStatus.UNPROCESSED
        );

        if (!unprocessed.isEmpty()) {
            log.info("Retrying {} unprocessed articles", unprocessed.size());
            unprocessed.forEach(processingService::processArticle);
        }
    }
}