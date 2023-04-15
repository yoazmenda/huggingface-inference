package com.yoazmenda;

import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertFalse;

public class HuggingFaceInferenceTest {

    @Test(enabled = false)
    public void testInference() throws IOException {
        String API_KEY = System.getenv("HF_API_KEY");
        HuggingFaceInference inference = new HuggingFaceInference("gpt2", API_KEY, 0D, 10);

        String inputs = "hello";

        String result = inference.infer(inputs);
        System.out.println(result);
        assertFalse(result.isEmpty());
    }

}