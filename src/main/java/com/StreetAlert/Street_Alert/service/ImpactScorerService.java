package com.StreetAlert.Street_Alert.service;

import com.StreetAlert.Street_Alert.entity.Article;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// service/ImpactScorerService.java
@Service
public class ImpactScorerService {

    private static final List<String> HIGH_IMPACT_WORDS = List.of(
            "crash", "surge", "ban", "collapse", "record high", "record low",
            "crisis", "emergency", "bankrupt", "fraud", "scam", "sanction",
            "war", "recession", "default", "bubble", "lawsuit", "probe",
            "investigation", "antitrust", "recall", "downgrade"
    );

    private static final List<String> TRUSTED_SOURCES = List.of(
            "reuters", "bloomberg", "cnbc", "financial times", "wsj",
            "marketwatch", "associated press", "ap news", "new york times",
            "the wall street journal", "the economist", "yahoo finance"
    );

    public Article.ImpactLevel score(String title, String description,
                                     String source, LocalDateTime publishedAt) {
        int points = 0;
        String text = ((title != null ? title : "") + " " +
                (description != null ? description : "")).toLowerCase();

        // high impact words in text
        for (String word : HIGH_IMPACT_WORDS) {
            if (text.contains(word)) { points += 3; break; }
        }

        // percentage change mentioned (e.g. "rises 8%", "falls 12%")
        if (text.matches(".*\\d+(\\.\\d+)?%.*")) points += 2;

        // mentions central banks
        if (text.contains("federal reserve") || text.contains("fomc")
                || text.contains("fed ") || text.contains("treasury")
                || text.contains("sec")) points += 2;

        // trusted source
        if (source != null) {
            String srcLower = source.toLowerCase();
            for (String trusted : TRUSTED_SOURCES) {
                if (srcLower.contains(trusted)) { points += 1; break; }
            }
        }

        // fresh news (published within last 2 hours)
        if (publishedAt != null &&
                publishedAt.isAfter(LocalDateTime.now().minusHours(2))) points += 1;

        if (points >= 5) return Article.ImpactLevel.HIGH;
        if (points >= 3) return Article.ImpactLevel.MEDIUM;
        return Article.ImpactLevel.LOW;
    }
}