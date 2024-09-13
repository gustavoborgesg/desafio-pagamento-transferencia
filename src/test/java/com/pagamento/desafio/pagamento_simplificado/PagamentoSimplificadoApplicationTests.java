package com.pagamento.desafio.pagamento_simplificado;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@ActiveProfiles("test")
class PagamentoSimplificadoApplicationTests {
    @ClassRule
    public static BaseIntegrationTest postgreSQLContainer = BaseIntegrationTest.getInstance();

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    public static void startContainer() {
        postgreSQLContainer.start();
    }

//    @Test
//    void contextLoads() {
//        // Verifica se o contexto da aplicação é carregado corretamente
//    }
}
