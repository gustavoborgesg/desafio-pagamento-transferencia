package com.pagamento.desafio.pagamento_simplificado.domain.validations;

import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.merchant.MerchantAlreadyExistsException;
import com.pagamento.desafio.pagamento_simplificado.repositories.MerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MerchantValidator implements Validator<Merchant> {

    private final MerchantRepository merchantRepository;

    @Override
    public void validate(Merchant merchant) {
        if (merchantRepository.findByEmail(merchant.getEmail()).isPresent()) {
            throw new MerchantAlreadyExistsException("Merchant with email " + merchant.getEmail() + " already exists.");
        }
        if (merchantRepository.findByCnpj(merchant.getCnpj()).isPresent()) {
            throw new MerchantAlreadyExistsException("Merchant with CNPJ " + merchant.getCnpj() + " already exists.");
        }
    }
}
