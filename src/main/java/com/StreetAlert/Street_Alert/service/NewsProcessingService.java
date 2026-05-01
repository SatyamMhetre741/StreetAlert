package com.StreetAlert.Street_Alert.service;

import com.StreetAlert.Street_Alert.entity.Article;
import com.StreetAlert.Street_Alert.event.ArticleSavedEvent;
import com.StreetAlert.Street_Alert.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

// service/NewsProcessingService.java
@Service
@Slf4j
public class NewsProcessingService {

    private final ArticleRepository articleRepository;
    private final SectorClassifierService classifierService;
    private final ImpactScorerService impactScorerService;
    private final NotificationService notificationService;

    public NewsProcessingService(ArticleRepository articleRepository,
                                 SectorClassifierService classifierService,
                                 ImpactScorerService impactScorerService,
                                 NotificationService notificationService) {
        this.articleRepository = articleRepository;
        this.classifierService = classifierService;
        this.impactScorerService = impactScorerService;
        this.notificationService = notificationService;
    }

    @EventListener
    @Async
    public void handleArticleSaved(ArticleSavedEvent event) {
        processArticle(event.getArticle());
    }

    // also called by ArticleProcessorJob for retries
    public void processArticle(Article article) {
        try {
            Article.Sector sector = classifierService.classify(
                    article.getTitle(), article.getDescription()
            );

            Article.ImpactLevel impact = impactScorerService.score(
                    article.getTitle(), article.getDescription(),
                    article.getSource(), article.getPublishedAt()
            );

            article.setSector(sector);
            article.setImpactLevel(impact);
            article.setStatus(Article.ArticleStatus.PROCESSED);
            articleRepository.save(article);

            log.info("Processed article [{}] → sector: {}, impact: {}",
                    article.getId(), sector, impact);

            // trigger notification dispatch
            notificationService.dispatchNotifications(article);

        } catch (Exception e) {
            log.error("Failed to process article [{}]: {}", article.getId(), e.getMessage());
            article.setStatus(Article.ArticleStatus.FAILED);
            articleRepository.save(article);
        }
    }
}