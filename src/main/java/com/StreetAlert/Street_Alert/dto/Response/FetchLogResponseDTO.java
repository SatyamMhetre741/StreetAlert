package com.StreetAlert.Street_Alert.dto.Response;

import com.StreetAlert.Street_Alert.entity.FetchLogs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class FetchLogResponseDTO {
    private Long id;
    private LocalDateTime fetchedAt;
    private Integer articlesFetched;
    private Integer articlesSaved;
    private String status;
    private String errorMessage;

    public static FetchLogResponseDTO from(FetchLogs log) {
        return FetchLogResponseDTO.builder()
                .id(log.getId())
                .fetchedAt(log.getFetchedAt())
                .articlesFetched(log.getArticlesFetched())
                .articlesSaved(log.getArticlesSaved())
                .status(log.getStatus())
                .errorMessage(log.getErrorMessage())
                .build();
    }
}
