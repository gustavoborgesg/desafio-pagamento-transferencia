package com.pagamento.desafio.pagamento_simplificado.domain.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.domain.exceptions.merchant.MerchantNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.domain.services.MerchantService;
import com.pagamento.desafio.pagamento_simplificado.domain.validations.Validator;
import com.pagamento.desafio.pagamento_simplificado.repositories.MerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator<Merchant> merchantValidator;

    @Override
    public Merchant registerMerchant(Merchant merchant) {
        merchantValidator.validate(merchant);
        merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
        return merchantRepository.save(merchant);
    }

    @Override
    public Merchant getMerchantById(Long id) {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new MerchantNotFoundException("Merchant with id " + id + " not found."));
    }

    @Override
    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    @Override
    public Merchant updateMerchant(Long id, Merchant updatedMerchant) {
        Merchant existingMerchant = getMerchantById(id);

        existingMerchant.setName(updatedMerchant.getName());
        existingMerchant.setEmail(updatedMerchant.getEmail());
        existingMerchant.setCnpj(updatedMerchant.getCnpj());

        if (updatedMerchant.getPassword() != null) {
            existingMerchant.setPassword(passwordEncoder.encode(updatedMerchant.getPassword()));
        }

        return merchantRepository.save(existingMerchant);
    }

    @Override
    public Merchant partialUpdateMerchant(Long id, MerchantUpdateRequest merchantUpdateRequest) {
        Merchant existingMerchant = getMerchantById(id);

        if (merchantUpdateRequest.getEmail() != null) {
            existingMerchant.setEmail(merchantUpdateRequest.getEmail());
        }
        if (merchantUpdateRequest.getName() != null) {
            existingMerchant.setName(merchantUpdateRequest.getName());
        }
        if (merchantUpdateRequest.getPassword() != null) {
            existingMerchant.setPassword(passwordEncoder.encode(merchantUpdateRequest.getPassword()));
        }

        return merchantRepository.save(existingMerchant);
    }

    @Override
    public void deleteMerchant(Long id) {
        Merchant merchant = getMerchantById(id);
        merchantRepository.delete(merchant);
    }
}
