package com.yoazmenda;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HuggingFaceInference {
    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceInference.class);

    private static final String API_URL = "https://api-inference.huggingface.co/models/";
    private final OkHttpClient client;
    private final String repoId;
    private final String apiKey;
    private final Double temperature;
    private final int maxLength;
    private final int maxRetries;
    private final long retryDelay;

    private HuggingFaceInference(String repoId, String apiKey, Double temperature, int maxLength,
                                 int maxRetries, long retryDelay) {
        this.repoId = repoId;
        this.apiKey = apiKey;
        this.temperature = temperature;
        this.maxLength = maxLength;
        this.maxRetries = maxRetries;
        this.retryDelay = retryDelay;
        this.client = new OkHttpClient();
    }

    public static class Builder {
        private final String repoId;
        private final String apiKey;
        private Double temperature;
        private int maxLength = 100;
        private int maxRetries = 3;
        private long retryDelay = 1000;

        public Builder(String repoId, String apiKey) {
            this.repoId = repoId;
            this.apiKey = apiKey;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder retryDelay(long retryDelay) {
            this.retryDelay = retryDelay;
            return this;
        }

        public HuggingFaceInference build() {
            return new HuggingFaceInference(repoId, apiKey, temperature, maxLength, maxRetries, retryDelay);
        }
    }

    public String infer(String inputs) throws IOException {
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be empty");
        }
        String url = API_URL + repoId;

        // Create the JSON payload using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> payload = new HashMap<>();
        payload.put("inputs", inputs);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("max_length", maxLength);
        if (temperature != null) {
            parameters.put("temperature", temperature);
        }
        payload.put("parameters", parameters);

        String requestParams = objectMapper.writeValueAsString(payload);
        logger.debug("request params: {}", requestParams);
        RequestBody requestBody = RequestBody.create(requestParams, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();

        int retries = 0;
        logger.info("calling HuggingFace Inference endpoint with text={}", inputs);
        while (true) {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    logger.debug("response successful: {}", response.body());
                    return response.body().string();
                } else if (retries < maxRetries) {
                    logger.debug("response not successful: {}", response.body());
                    logger.debug("retrying...");
                    retries++;
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    logger.error("Failed to query HuggingFace Inference with response: {}", response);
                    throw new IOException("Unexpected response code: " + response.code());
                }
            }
        }
    }
}
