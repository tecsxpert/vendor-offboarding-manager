package com.internship.tool.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;

public class AiServiceClient {

    private final RestTemplate restTemplate;

    public AiServiceClient() {
        this.restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(java.time.Duration.ofSeconds(10))
                .setReadTimeout(java.time.Duration.ofSeconds(10))
                .build();
    }

    // ----------- /describe -----------
    public String getDescription(String input) {
        String url = "http://localhost:5000/describe";
        return callApi(url, input);
    }

    // ----------- /recommend -----------
    public String getRecommendation(String input) {
        String url = "http://localhost:5000/recommend";
        return callApi(url, input);
    }

    // ----------- /generate-report -----------
    public String generateReport(String input) {
        String url = "http://localhost:5000/generate-report";
        return callApi(url, input);
    }

    // ----------- COMMON METHOD -----------
    private String callApi(String url, String input) {

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("input", input);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map responseBody = response.getBody();
                return (String) responseBody.get("response");
            }

        } catch (ResourceAccessException e) {
            System.out.println("Timeout or connection error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error calling AI service: " + e.getMessage());
        }

        return null; // required
    }
}