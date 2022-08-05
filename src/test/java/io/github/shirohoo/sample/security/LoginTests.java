package io.github.shirohoo.sample.security;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import io.github.shirohoo.sample.security.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = RANDOM_PORT)
class LoginTests {
    WebTestClient webTestClient;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        webTestClient = MockMvcWebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .build();
    }

    @Test
    void shouldBeSuccessfulLogin() {
        webTestClient.post()
                .uri("http://localhost:8080/api/v1/login")
                .contentType(APPLICATION_JSON)
                .bodyValue(new LoginRequest("siro@gmail.com", "aaaaaaaaaaaa@#!123456"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }
}


