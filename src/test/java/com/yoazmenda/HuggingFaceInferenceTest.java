package com.yoazmenda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;

public class HuggingFaceInferenceTest {

    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceInferenceTest.class);

    private static final String API_KEY = System.getenv("HF_API_KEY");

    @Test
    public void testInference() throws IOException {
        HuggingFaceInference inference = new HuggingFaceInference.Builder("gpt2", API_KEY)
                .maxRetries(0)
                .build();
        String inputs = "hello";
        String result = inference.infer(inputs);
        logger.info("result: {}", result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testInferenceWithTemperature() throws IOException {
        HuggingFaceInference inference = new HuggingFaceInference.Builder("gpt2", API_KEY)
                .maxRetries(0)
                .temperature(1.0)
                .build();
        String inputs = "hello";
        String result = inference.infer(inputs);
        logger.info("result: {}", result);
        assertFalse(result.isEmpty());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testEmptyInput() throws IOException {
        HuggingFaceInference inference = new HuggingFaceInference.Builder("gpt2", API_KEY)
                .maxLength(10)
                .build();
        String inputs = "";
        String result = inference.infer(inputs);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testInvalidApiKey() {
        HuggingFaceInference inference = new HuggingFaceInference.Builder("gpt2", "invalid-api-key")
                .maxLength(10)
                .maxRetries(0)
                .build();
        String inputs = "hello";
        assertThrows(IOException.class, () -> inference.infer(inputs));
    }

    @Test
    public void testMaxRetries() throws IOException {
        HuggingFaceInference inference = new HuggingFaceInference.Builder("gpt2", API_KEY)
                .maxLength(10)
                .maxRetries(3)
                .retryDelay(1000)
                .build();
        String inputs = "hello";
        String result = inference.infer(inputs);
        assertFalse(result.isEmpty());
    }
}
