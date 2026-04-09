package com.StreetAlert.Street_Alert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

@Service
public class APIService {

    private final SecretsManagerClient client;
    private final ObjectMapper objectMapper;
    private final String secretName;
    private final String apiKeyField;

    public APIService(
            @Value("${aws.secretsmanager.region}") String region,
            @Value("${aws.secretsmanager.secret-name}") String secretName,
            @Value("${aws.secretsmanager.api-key-field:apiKey}") String apiKeyField,
            ObjectMapper objectMapper
    ) {
        this.secretName = secretName;
        this.apiKeyField = apiKeyField;
        this.objectMapper = objectMapper;
        this.client = SecretsManagerClient.builder()
                .region(Region.of(region))
                .build();
    }

    public String getApiKey() {
        GetSecretValueResponse response;
        try {
            response = client.getSecretValue(
                    GetSecretValueRequest.builder()
                            .secretId(secretName)
                            .build()
            );
        } catch (SecretsManagerException e) {
            throw new IllegalStateException("Failed to fetch secret from AWS Secrets Manager: " + secretName, e);
        }

        String secretString = response.secretString();
        if (secretString == null || secretString.isBlank()) {
            throw new IllegalStateException("Secret string is empty for secret: " + secretName);
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(secretString);
            JsonNode apiKeyNode = jsonNode.get(apiKeyField);
            if (apiKeyNode == null || apiKeyNode.asText().isBlank()) {
                throw new IllegalStateException("Secret does not contain a non-empty key field: " + apiKeyField);
            }
            return apiKeyNode.asText();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse secret JSON for secret: " + secretName, e);
        }
    }
}
