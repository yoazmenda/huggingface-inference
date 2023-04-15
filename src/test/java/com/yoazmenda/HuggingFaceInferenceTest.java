package com.yoazmenda;

import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertFalse;

public class HuggingFaceInferenceTest {

    @Test
    public void testInference() throws IOException {
        String API_TOKEN = System.getenv("HF_API_KEY");
        HuggingFaceInference inference = new HuggingFaceInference("gpt2", API_TOKEN);

        String inputs = "hello";
        double temperature = 0;
        int maxLength = 1;

        String result = inference.infer(inputs, temperature, maxLength);
        assertFalse(result.isEmpty());
    }

}