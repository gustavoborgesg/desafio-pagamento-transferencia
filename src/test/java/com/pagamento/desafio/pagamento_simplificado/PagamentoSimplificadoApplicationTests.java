package com.pagamento.desafio.pagamento_simplificado;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PagamentoSimplificadoApplicationTests extends BaseIntegrationTest {
    private static final PostgreSQLContainer<?> postgreSQLContainer = BaseIntegrationTest.getInstance();

    @Test
    void contextLoads() {
    }

    @Test
    void connectionEstablished() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }
}
