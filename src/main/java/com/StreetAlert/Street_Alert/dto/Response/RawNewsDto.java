package com.StreetAlert.Street_Alert.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawNewsDto {

    private String title;
    private String description;
    private String url;
    private String content;
    private String publishedAt;

    @JsonProperty("source")
    private SourceDto source;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceDto {
        private String name;
    }
}
