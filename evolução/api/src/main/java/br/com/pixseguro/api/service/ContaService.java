package br.com.pixseguro.api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pixseguro.api.model.Conta;
import br.com.pixseguro.api.repository.ContaRepository;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Conta criarConta(Conta conta) {
        conta.setSaldo(BigDecimal.ZERO);
        conta.setScoreDeRisco(0);
        conta.setDataCriacao(LocalDateTime.now());
        
        return contaRepository.save(conta);
    }

    /**
     * Adiciona um valor ao saldo de uma conta existente.
     * @param contaId O ID da conta.
     * @param valor O valor a ser depositado (deve ser positivo).
     * @return A conta com o saldo atualizado.
     */
    @Transactional
    public Conta adicionarSaldo(Long contaId, BigDecimal valor) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada!"));
        
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do depósito deve ser positivo.");
        }

        conta.setSaldo(conta.getSaldo().add(valor));
        return contaRepository.save(conta);
    }
}