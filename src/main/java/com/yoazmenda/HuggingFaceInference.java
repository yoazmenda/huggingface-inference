package com.yoazmenda;

import com.squareup.okhttp.*;

import java.io.IOException;

public class HuggingFaceInference {
    private final String API_KEY;
    private final String repoId;
    private final OkHttpClient client;
    private final Double temperature;
    private final Integer maxTokens;

    public HuggingFaceInference(String repoId, String apiKey, Double temperature, Integer maxTokens) {
        this.repoId = repoId;
        this.API_KEY = apiKey;
        this.client = new OkHttpClient();
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }

    public String infer(String inputs) throws IOException {
        String url = "https://api-inference.huggingface.co/models/" + repoId;
        String jsonInput = String.format("{\"inputs\":\"%s\", \"temperature\":%.2f, \"max_length\":%d}", inputs, temperature, maxTokens);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonInput);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected response code: " + response.code());
        }
    }
}
