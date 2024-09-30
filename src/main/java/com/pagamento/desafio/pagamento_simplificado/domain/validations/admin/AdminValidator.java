package com.pagamento.desafio.pagamento_simplificado.domain.validations.admin;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Admin;
import com.pagamento.desafio.pagamento_simplificado.domain.validations.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminValidator implements Validator<Admin> {

    private final AdminEmailValidator emailValidator;
    private final AdminCpfValidator cpfValidator;

    @Override
    public void validate(Admin admin) {
        emailValidator.validate(admin.getEmail());
        cpfValidator.validate(admin.getCpf());
    }
}
