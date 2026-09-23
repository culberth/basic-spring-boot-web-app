package com.example.controlsdemo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControlsDemoApplicationTests {
    @LocalServerPort
    private int port;

    private static final String VALID_FORM = """
            {"name":"Alex","language":"Java","enthusiasm":75,"date":"2026-09-22",
             "theme":"Dark","notifications":true,"notes":"Hello Spring!"}
            """;

    @Test
    void servesTheHomePage() throws Exception {
        try (var client = HttpClient.newHttpClient()) {
            var response = client.send(HttpRequest.newBuilder(url("/")).GET().build(),
                    HttpResponse.BodyHandlers.ofString());
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(response.body()).contains("A little control room.", "id=\"demo-form\"");
        }
    }

    @Test
    void echoesValidFormValues() throws Exception {
        var response = post(VALID_FORM);
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"name\":\"Alex\"", "\"enthusiasm\":75",
                "\"notifications\":true", "\"date\":\"2026-09-22\"", "Hello Spring!");
    }

    @Test
    void rejectsInvalidInput() throws Exception {
        assertThat(post(VALID_FORM.replace("Alex", " ")).statusCode()).isEqualTo(400);
        assertThat(post(VALID_FORM.replace("75", "101")).statusCode()).isEqualTo(400);
        assertThat(post(VALID_FORM.replace("Java", "Unknown")).statusCode()).isEqualTo(400);
        assertThat(post(VALID_FORM.replace("2026-09-22", "not-a-date")).statusCode()).isEqualTo(400);
    }

    private HttpResponse<String> post(String json) throws Exception {
        try (var client = HttpClient.newHttpClient()) {
            return client.send(HttpRequest.newBuilder(url("/api/demo"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json)).build(), HttpResponse.BodyHandlers.ofString());
        }
    }

    private URI url(String path) {
        return URI.create("http://localhost:" + port + path);
    }
}
