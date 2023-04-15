package com.yoazmenda;

import com.squareup.okhttp.*;

import java.io.IOException;

public class HuggingFaceInference {
    private final String API_TOKEN;
    private final String repoId;
    private final OkHttpClient client;

    public HuggingFaceInference(String repoId, String API_TOKEN) {
        this.repoId = repoId;
        this.API_TOKEN = API_TOKEN;
        this.client = new OkHttpClient();
    }

    public String infer(String inputs, double temperature, int maxLength) throws IOException {
        String url = "https://api-inference.huggingface.co/models/" + repoId;
        String jsonInput = String.format("{\"inputs\":\"%s\", \"temperature\":%.2f, \"max_length\":%d}", inputs, temperature, maxLength);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonInput);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + API_TOKEN)
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
