package com.StreetAlert.Street_Alert.dto.Request;

import com.StreetAlert.Street_Alert.entity.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KeywordRuleRequestDTO {
    @NotNull
    private Article.Sector sector;

    @NotBlank
    private String keyword;

    @NotNull
    private Integer impactScore;
}
