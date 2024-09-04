package com.pagamento.desafio.pagamento_simplificado.domain.validations;

public interface Validator<T> {
    void validate(T entity);
}
