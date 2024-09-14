package com.pagamento.desafio.pagamento_simplificado;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class PagamentoSimplificadoApplicationTests {
    private static final PostgreSQLContainer<?> postgreSQLContainer = BaseIntegrationTest.getInstance();
    private static final String AUTHORIZATION_URL = "https://test-authorization-url.com";
    private static final String NOTIFICATION_URL = "https://test-notification-url.com";

    @BeforeAll
    public static void startContainer() {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

        registry.add("AUTHORIZATION_URL", () -> AUTHORIZATION_URL);
        registry.add("NOTIFICATION_URL", () -> NOTIFICATION_URL);
    }

    @Test
    void contextLoads() {
    }
}
