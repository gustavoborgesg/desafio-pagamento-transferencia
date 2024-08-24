package com.pagamento.desafio.pagamento_simplificado.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.exceptions.merchant.MerchantAlreadyExistsException;
import com.pagamento.desafio.pagamento_simplificado.exceptions.merchant.MerchantNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.repositories.MerchantRepository;
import com.pagamento.desafio.pagamento_simplificado.services.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Merchant registerMerchant(Merchant merchant) {
        validateMerchantUniqueness(merchant);
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
        updateMerchantFields(existingMerchant, updatedMerchant);
        return merchantRepository.save(existingMerchant);
    }

    @Override
    public Merchant partialUpdateMerchant(Long id, MerchantUpdateRequest merchantUpdateRequest) {
        Merchant existingMerchant = getMerchantById(id);
        updateMerchantFields(existingMerchant, merchantUpdateRequest);
        return merchantRepository.save(existingMerchant);
    }

    @Override
    public void deleteMerchant(Long id) {
        Merchant merchant = getMerchantById(id);
        merchantRepository.delete(merchant);
    }

    private void validateMerchantUniqueness(Merchant merchant) {
        if (merchantRepository.findByEmail(merchant.getEmail()).isPresent()) {
            throw new MerchantAlreadyExistsException("Merchant with email " + merchant.getEmail() + " already exists.");
        }
        if (merchantRepository.findByCnpj(merchant.getCnpj()).isPresent()) {
            throw new MerchantAlreadyExistsException("Merchant with CNPJ " + merchant.getCnpj() + " already exists.");
        }
    }

    private void updateMerchantFields(Merchant existingMerchant, Merchant updatedMerchant) {
        existingMerchant.setEmail(updatedMerchant.getEmail());
        existingMerchant.setName(updatedMerchant.getName());
        existingMerchant.setCnpj(updatedMerchant.getCnpj());
        if (updatedMerchant.getPassword() != null) {
            existingMerchant.setPassword(passwordEncoder.encode(updatedMerchant.getPassword()));
        }
    }

    private void updateMerchantFields(Merchant existingMerchant, MerchantUpdateRequest updateRequest) {
        if (updateRequest.getEmail() != null) {
            existingMerchant.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getName() != null) {
            existingMerchant.setName(updateRequest.getName());
        }
        if (updateRequest.getPassword() != null) {
            existingMerchant.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
    }
}
