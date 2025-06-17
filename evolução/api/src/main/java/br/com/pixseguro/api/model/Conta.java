package br.com.pixseguro.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity // 1. Avisa ao Spring que esta classe representa uma tabela no banco.
@Data   // 2. Mágica do Lombok: cria getters, setters e outros métodos para nós.
public class Conta {

    @Id // 3. Define que este campo é a chave primária da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. O banco de dados vai gerar o valor do ID automaticamente.
    private Long id;

    @Column(nullable = false) // 5. Define que este campo não pode ser nulo na tabela.
    private String nomeTitular;

    @Column(nullable = false, unique = true) // 6. Não pode ser nulo e deve ser único.
    private String cpf;

    @Column(nullable = false, unique = true) // 7. A chave PIX também deve ser única.
    private String chavePix;

    @Column(nullable = false)
    private BigDecimal saldo;

    // --- Campos para nossa análise de risco ---

    @Column(nullable = false)
    private Integer scoreDeRisco; // Um número de 0 a 100.

    @Column(nullable = false)
    private LocalDateTime dataCriacao;
}