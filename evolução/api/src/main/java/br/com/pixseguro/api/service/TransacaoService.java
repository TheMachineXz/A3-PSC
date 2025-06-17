package br.com.pixseguro.api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pixseguro.api.model.Conta;
import br.com.pixseguro.api.model.Transacao;
import br.com.pixseguro.api.repository.ContaRepository;
import br.com.pixseguro.api.repository.TransacaoRepository;

@Service
public class TransacaoService {
    // Definindo constantes para as nossas regras de risco, para facilitar a manutenção.
    private static final BigDecimal VALOR_ALTO_RISCO = new BigDecimal("1000.00");
    private static final int SCORE_ALTO_RISCO = 70;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Transactional
    public void realizarTransacao(Long idContaOrigem, String chavePixDestino, BigDecimal valor) {
        // Passos 1, 2 e 3
        Conta contaOrigem = contaRepository.findById(idContaOrigem)
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada!"));

        Conta contaDestino = contaRepository.findByChavePix(chavePixDestino)
                .orElseThrow(() -> new RuntimeException("Chave PIX de destino não encontrada!"));

        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new RuntimeException("A conta de origem e destino não podem ser a mesma.");
        }

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar a transação.");
        }

        // Passo 4: Analisar o risco da transação
        analisarRisco(contaDestino, valor);

        // Passo 5: Efetivar a transferência (Novo!)
        // Usamos subtract() para subtrair e add() para adicionar valores BigDecimal.
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        // Salvamos as contas com os saldos atualizados.
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        // Passo 6: Salvar o registro da transação (Novo!)
        Transacao novaTransacao = new Transacao();
        novaTransacao.setValor(valor);
        novaTransacao.setDataHora(LocalDateTime.now());
        novaTransacao.setContaOrigem(contaOrigem);
        novaTransacao.setContaDestino(contaDestino);
        
        transacaoRepository.save(novaTransacao);
    }

    /**
     * Analisa o risco de uma transação com base na conta de destino e no valor.
     * @param contaDestino A conta que irá receber o valor.
     * @param valor O montante a ser transferido.
     */
    private void analisarRisco(Conta contaDestino, BigDecimal valor) {
        // Regra 1: Conta de destino muito recente com transação de alto valor.
        long horasDesdeCriacao = ChronoUnit.HOURS.between(contaDestino.getDataCriacao(), LocalDateTime.now());
        if (horasDesdeCriacao < 24 && valor.compareTo(VALOR_ALTO_RISCO) >= 0) {
            // Aumenta o score de risco da conta e a salva.
            contaDestino.setScoreDeRisco(contaDestino.getScoreDeRisco() + 30);
            contaRepository.save(contaDestino);
            // Lança uma exceção para bloquear a transação.
            throw new RuntimeException("ALERTA DE SEGURANÇA: Transação de alto valor para uma conta recém-criada. Operação bloqueada.");
        }

        // Regra 2: Conta de destino já tem um score de risco elevado.
        if (contaDestino.getScoreDeRisco() > SCORE_ALTO_RISCO) {
            throw new RuntimeException("ALERTA DE SEGURANÇA: A conta de destino é considerada de alto risco. Operação bloqueada.");
        }
    }
}