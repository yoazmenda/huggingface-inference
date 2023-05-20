# HuggingFace-Inference Java client

A Java client library for the Hugging Face Inference API, enabling easy integration of models into Java-based applications.

## Features
- Easy integration with Hugging Face API
- Customizable parameters for inference, such as temperature and max length
- Built-in retry mechanism for API calls

## Getting Started

This section explains how to install and use the `huggingface-inference` library in your Java projects.

### Installation

Add the following dependency to your `pom.xml` file (the version number might change, so make sure to check the latest release on GitHub):

```xml
<dependency>
    <groupId>io.github.yoazmenda</groupId>
    <artifactId>huggingface-inference</artifactId>
    <version>1.0.0</version>
</dependency>
```

## usage
```java
import com.yoazmenda.HuggingFaceInference;
import java.io.IOException;

public class Example {

    public static void main(String[] args) throws IOException {
        // Replace API_KEY with your actual Hugging Face API key
        String API_KEY = "your-api-key-here";

        HuggingFaceInference inference = new HuggingFaceInference.Builder("gpt2", API_KEY).build();
        String inputs = "The meaning of life is: ";
        String result = inference.infer(inputs);
        System.out.println("result: " + result);
    }
}

```
