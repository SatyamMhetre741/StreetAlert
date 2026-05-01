package com.StreetAlert.Street_Alert.client;

import com.StreetAlert.Street_Alert.dto.Response.NewsApiResponse;
import com.StreetAlert.Street_Alert.dto.Response.RawNewsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class NewsApiClient {
    private final RestTemplate restTemplate;

    @Value("${news.api.url}")
    private String apiUrl;

    @Value("${news.api.key}")
    private String apiKey;

    public NewsApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RawNewsDto> fetchFinancialNews() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("country", "us")
                    .queryParam("category", "business")
                    .queryParam("apiKey", apiKey)
                    .build(true)
                    .toUriString();
            NewsApiResponse response = restTemplate.getForObject(url, NewsApiResponse.class);

            if (response == null || response.getArticles() == null) {
                log.warn("NewsAPI returned empty response");
                return Collections.emptyList();
            }

            log.info("Fetched {} articles from NewsAPI", response.getArticles().size());
            return response.getArticles();

        } catch (Exception e) {
            log.error("Failed to fetch from NewsAPI: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

}
