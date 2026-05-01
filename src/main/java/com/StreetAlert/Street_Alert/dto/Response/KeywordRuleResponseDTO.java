package com.StreetAlert.Street_Alert.dto.Response;

import com.StreetAlert.Street_Alert.entity.Article;
import com.StreetAlert.Street_Alert.entity.KeywordRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class KeywordRuleResponseDTO {
    private Long id;
    private Article.Sector sector;
    private String keyword;
    private Integer impactScore;
    private LocalDateTime createdAt;

    public static KeywordRuleResponseDTO from(KeywordRule rule) {
        return KeywordRuleResponseDTO.builder()
                .id(rule.getId())
                .sector(rule.getSector())
                .keyword(rule.getKeyword())
                .impactScore(rule.getImpactScore())
                .createdAt(rule.getCreatedAt())
                .build();
    }
}
