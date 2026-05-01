package com.StreetAlert.Street_Alert.scheduler;

import com.StreetAlert.Street_Alert.client.NewsApiClient;
import com.StreetAlert.Street_Alert.dto.Response.RawNewsDto;
import com.StreetAlert.Street_Alert.entity.Article;
import com.StreetAlert.Street_Alert.entity.FetchLogs;
import com.StreetAlert.Street_Alert.repository.ArticleRepository;
import com.StreetAlert.Street_Alert.repository.FetchLogRepository;
import com.StreetAlert.Street_Alert.service.DeDuplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// scheduler/NewsFetcherScheduler.java
@Component
@Slf4j
public class NewsFetcherScheduler {

    private final NewsApiClient newsApiClient;
    private final ArticleRepository articleRepository;
    private final DeDuplicationService deduplicationService;
    private final FetchLogRepository fetchLogRepository;

    public NewsFetcherScheduler(NewsApiClient newsApiClient,
                                ArticleRepository articleRepository,
                                DeDuplicationService deduplicationService,
                                FetchLogRepository fetchLogRepository) {
        this.newsApiClient = newsApiClient;
        this.articleRepository = articleRepository;
        this.deduplicationService = deduplicationService;
        this.fetchLogRepository = fetchLogRepository;
    }

    @Scheduled(fixedDelayString = "${news.fetch.interval-ms:3600000}", initialDelay = 10000)
    public void fetchAndStore() {
        log.info("=== News fetch job started ===");

        int fetched = 0;
        int saved = 0;
        String jobStatus = "SUCCESS";
        String errorMsg = null;

        try {
            List<RawNewsDto> articles = newsApiClient.fetchFinancialNews();
            fetched = articles.size();

            for (RawNewsDto raw : articles) {

                // skip if title or url missing
                if (raw.getUrl() == null || raw.getTitle() == null) continue;

                // skip if duplicate
                String urlHash = DigestUtils.md5DigestAsHex(raw.getUrl().getBytes(StandardCharsets.UTF_8));
                if (deduplicationService.isDuplicate(raw.getUrl())) continue;
                if (articleRepository.existsByUrlHash(urlHash)) continue;

                // build and save the article
                Article article = Article.builder()
                        .title(raw.getTitle())
                        .description(raw.getDescription())
                        .content(raw.getContent())
                        .url(raw.getUrl())
                        .urlHash(urlHash)
                        .source(raw.getSource() != null ? raw.getSource().getName() : "Unknown")
                        .status(Article.ArticleStatus.UNPROCESSED)
                        .publishedAt(parseDate(raw.getPublishedAt()))
                        .fetchedAt(LocalDateTime.now())
                        .build();

                articleRepository.save(article);
                deduplicationService.markAsSeen(raw.getUrl());

                saved++;
            }

        } catch (Exception e) {
            jobStatus = "FAILED";
            errorMsg = e.getMessage();
            log.error("News fetch job failed: {}", e.getMessage());
        }

        // log the result
        fetchLogRepository.save(FetchLogs.builder()
                .fetchedAt(LocalDateTime.now())
                .articlesFetched(fetched)
                .articlesSaved(saved)
                .status(jobStatus)
                .errorMessage(errorMsg)
                .build());

        log.info("=== Fetch done — fetched: {}, saved: {} ===", fetched, saved);
    }

    private LocalDateTime parseDate(String dateStr) {
        if (dateStr == null) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}