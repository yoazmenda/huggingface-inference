package com.yoazmenda;

import okhttp3.*;
import java.io.IOException;

public class HuggingFaceInference {
    private final String API_KEY;
    private final String repoId;
    private final OkHttpClient client;
    private final Double temperature;
    private final Integer maxTokens;
    private final int maxRetries;
    private final int retryDelay;

    private HuggingFaceInference(String repoId, String apiKey, Double temperature, Integer maxTokens, int maxRetries, int retryDelay) {
        this.repoId = repoId;
        this.API_KEY = apiKey;
        this.client = new OkHttpClient();
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.maxRetries = maxRetries;
        this.retryDelay = retryDelay;
    }

    public static class Builder {
        private String repoId;
        private String apiKey;
        private Double temperature;
        private Integer maxTokens;
        private int maxRetries = 0;
        private int retryDelay = 0;

        public Builder(String repoId, String apiKey) {
            this.repoId = repoId;
            this.apiKey = apiKey;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder retryDelay(int retryDelay) {
            this.retryDelay = retryDelay;
            return this;
        }

        public HuggingFaceInference build() {
            return new HuggingFaceInference(repoId, apiKey, temperature, maxTokens, maxRetries, retryDelay);
        }
    }

    public String infer(String inputs) throws IOException {
        String url = "https://api-inference.huggingface.co/models/" + repoId;
        String jsonInput = String.format("{\"inputs\":\"%s\", \"temperature\":%.2f, \"max_length\":%d}", inputs, temperature, maxTokens);
        RequestBody requestBody = RequestBody.create(jsonInput, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(requestBody)
                .build();

        int retries = 0;
        while (true) {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    return response.body().string();
                } else if (retries < maxRetries) {
                    retries++;
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw new IOException("Unexpected response code: " + response.code());
                }
            }
        }
    }
}
