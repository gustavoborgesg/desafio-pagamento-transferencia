package com.pagamento.desafio.pagamento_simplificado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class PagamentoSimplificadoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PagamentoSimplificadoApplication.class, args);
    }

}
