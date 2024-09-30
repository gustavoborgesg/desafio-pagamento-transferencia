package com.pagamento.desafio.pagamento_simplificado;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIntegrationTest {
    private static final String IMAGE_VERSION = "postgres:16.4";
    private static final String AUTHORIZATION_URL = "https://test-authorization-url.com";
    private static final String NOTIFICATION_URL = "https://test-notification-url.com";

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(IMAGE_VERSION);

    @BeforeAll
    public static void startContainer() {
        if (!container.isRunning()) {
            container.start();
        }
    }

    @DynamicPropertySource
    static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);

        registry.add("AUTHORIZATION_URL", () -> AUTHORIZATION_URL);
        registry.add("NOTIFICATION_URL", () -> NOTIFICATION_URL);
    }

    public static PostgreSQLContainer<?> getInstance() {
        return container;
    }
}
