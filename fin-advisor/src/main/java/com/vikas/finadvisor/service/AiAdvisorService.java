package com.vikas.finadvisor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiAdvisorService {

    @Value("${ai.api.key}")
    private String apiKey;

    // ✅ Correct Gemini API endpoint (v1beta and latest model)
    private static final String MODEL_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    private final RestTemplate restTemplate;

    public AiAdvisorService() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory());
    }

    public String generateAdvice(String question) {
        try {
            // 🧠 Short, structured prompt with emoji tone
            String prompt = "You are a friendly AI Personal Finance Advisor. "
                    + "Respond in 3-6 short bullet points with emojis. "
                    + "Be concise, practical, and easy to read.\n\n"
                    + "User question: " + question;

            // 🔹 Request body for Gemini API
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> content = new HashMap<>();
            content.put("parts", new Object[]{Map.of("text", prompt)});
            requestBody.put("contents", new Object[]{content});

            // 🔹 Headers (no Bearer, Gemini expects key in URL)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // ✅ Append key as query parameter
            String url = MODEL_URL + "?key=" + apiKey;

            // 🔹 Make POST request
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // 🔹 Parse and extract Gemini response
            if (response.getBody() != null && response.getBody().get("candidates") != null) {
                var candidates = (java.util.List<?>) response.getBody().get("candidates");
                if (!candidates.isEmpty()) {
                    var contentMap = (Map<?, ?>) ((Map<?, ?>) candidates.get(0)).get("content");
                    var parts = (java.util.List<?>) contentMap.get("parts");
                    if (!parts.isEmpty()) {
                        var text = ((Map<?, ?>) parts.get(0)).get("text");
                        return text.toString();
                    }
                }
            }

            return "Sorry, I couldn’t generate advice right now. Try again later.";

        } catch (Exception e) {
            System.out.println("❌ Gemini API Error: " + e.getMessage());
            return "Oops! Something went wrong while generating advice. Please check your API key or internet connection.";
        }
    }
}
