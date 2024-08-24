package com.pagamento.desafio.pagamento_simplificado.services.implementations;

import com.pagamento.desafio.pagamento_simplificado.controllers.dtos.merchant.MerchantUpdateRequest;
import com.pagamento.desafio.pagamento_simplificado.domain.entities.Merchant;
import com.pagamento.desafio.pagamento_simplificado.exceptions.merchant.MerchantAlreadyExistsException;
import com.pagamento.desafio.pagamento_simplificado.exceptions.merchant.MerchantNotFoundException;
import com.pagamento.desafio.pagamento_simplificado.exceptions.merchant.MerchantOperationException;
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
    public void registerMerchant(Merchant merchant) {
        if (merchantRepository.findByEmail(merchant.getEmail()).isPresent()) {
            throw new MerchantAlreadyExistsException("Merchant with email " + merchant.getEmail() + " already exists.");
        }

        merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
        merchantRepository.save(merchant);
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
        try {
            Merchant existingMerchant = getMerchantById(id);
            existingMerchant.setEmail(updatedMerchant.getEmail());
            existingMerchant.setName(updatedMerchant.getName());
            if (updatedMerchant.getPassword() != null) {
                existingMerchant.setPassword(passwordEncoder.encode(updatedMerchant.getPassword()));
            }
            return merchantRepository.save(existingMerchant);
        } catch (Exception e) {
            throw new MerchantOperationException("Failed to update merchant with id " + id);
        }
    }

    @Override
    public Merchant partialUpdateMerchant(Long id, MerchantUpdateRequest merchantUpdateRequest) {
        try {
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
        } catch (Exception e) {
            throw new MerchantOperationException("Failed to partially update merchant with id " + id);
        }
    }

    @Override
    public void deleteMerchant(Long id) {
        Merchant merchant = getMerchantById(id);
        try {
            merchantRepository.delete(merchant);
        } catch (Exception e) {
            throw new MerchantOperationException("Failed to delete merchant with id " + id);
        }
    }
}
