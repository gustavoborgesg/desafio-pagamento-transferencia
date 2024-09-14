package com.pagamento.desafio.pagamento_simplificado;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class BaseIntegrationTest {
    private static final String IMAGE_VERSION = "postgres:15.2";
    private static final String DATABASE_NAME = "test";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(IMAGE_VERSION)
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    @BeforeAll
    public static void startContainer() {
        if (!container.isRunning()) {
            container.start();
        }
    }

    @AfterAll
    public static void stopContainer() {
        if (container.isRunning()) {
            container.stop();
        }
    }

    public static PostgreSQLContainer<?> getInstance() {
        return container;
    }
}
