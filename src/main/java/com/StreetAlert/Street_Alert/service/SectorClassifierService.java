package com.StreetAlert.Street_Alert.service;

import com.StreetAlert.Street_Alert.entity.Article;
import com.StreetAlert.Street_Alert.entity.KeywordRule;
import com.StreetAlert.Street_Alert.repository.KeywordRuleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

// service/SectorClassifierService.java
@Service
@Slf4j
public class SectorClassifierService {

    private final KeywordRuleRepository keywordRuleRepository;

    // cache keyword rules in memory after first load
    private Map<Article.Sector, List<String>> sectorKeywordMap;

    public SectorClassifierService(KeywordRuleRepository keywordRuleRepository) {
        this.keywordRuleRepository = keywordRuleRepository;
    }

    @PostConstruct
    public void loadKeywords() {
        sectorKeywordMap = new EnumMap<>(Article.Sector.class);
        List<KeywordRule> rules = keywordRuleRepository.findAll();

        for (KeywordRule rule : rules) {
            sectorKeywordMap
                    .computeIfAbsent(rule.getSector(), k -> new ArrayList<>())
                    .add(rule.getKeyword().toLowerCase());
        }

        log.info("Loaded {} keyword rules for classification", rules.size());
    }

    public Article.Sector classify(String title, String description) {
        String text = ((title != null ? title : "") + " " +
                (description != null ? description : "")).toLowerCase();

        Map<Article.Sector, Integer> scores = new EnumMap<>(Article.Sector.class);

        for (Map.Entry<Article.Sector, List<String>> entry : sectorKeywordMap.entrySet()) {
            int score = 0;
            for (String keyword : entry.getValue()) {
                if (text.contains(keyword)) score++;
            }
            if (score > 0) scores.put(entry.getKey(), score);
        }

        return scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Article.Sector.GENERAL);
    }
}