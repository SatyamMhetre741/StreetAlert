package com.StreetAlert.Street_Alert.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsApiResponse {
    private String status;
    private Integer totalResults;
    private List<RawNewsDto> articles;
}

