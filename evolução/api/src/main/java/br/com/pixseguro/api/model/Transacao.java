package br.com.pixseguro.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;

    private LocalDateTime dataHora;

    // --- Relacionamentos (Novo Conceito!) ---

    @ManyToOne // 1. Anotação que define um relacionamento: Muitas transações pertencem a UMA conta.
    @JoinColumn(name = "conta_origem_id") // 2. Especifica o nome da coluna no banco que guardará o ID da conta de origem.
    private Conta contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    private Conta contaDestino;
}