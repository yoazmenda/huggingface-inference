package com.yoazmenda;

import org.testng.annotations.Test;
import java.io.IOException;
import static org.testng.Assert.*;

public class HuggingFaceInferenceTest {

    private static final String API_KEY = System.getenv("HF_API_KEY");

    @Test
    public void testInference() throws IOException {
        HuggingFaceInference inference = new HuggingFaceInference.Builder("gpt2", API_KEY)
                .maxTokens(10)
                .build();
        String inputs = "hello";
        String result = inference.infer(inputs);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testEmptyInput() throws IOException {
        HuggingFaceInference inference = new HuggingFaceInference.Builder("gpt2", API_KEY)
                .maxTokens(10)
                .build();
        String inputs = "";
        String result = inference.infer(inputs);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testInvalidApiKey() {
        HuggingFaceInference inference = new HuggingFaceInference.Builder("gpt2", "invalid-api-key")
                .maxTokens(10)
                .build();
        String inputs = "hello";
        assertThrows(IOException.class, () -> inference.infer(inputs));
    }

    @Test
    public void testMaxRetries() throws IOException {
        HuggingFaceInference inference = new HuggingFaceInference.Builder("gpt2", API_KEY)
                .maxTokens(10)
                .maxRetries(3)
                .retryDelay(1000)
                .build();
        String inputs = "hello";
        String result = inference.infer(inputs);
        assertFalse(result.isEmpty());
    }
}
