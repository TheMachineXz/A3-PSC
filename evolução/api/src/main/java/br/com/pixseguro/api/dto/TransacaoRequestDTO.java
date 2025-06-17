package br.com.pixseguro.api.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data // O Lombok gera os getters e setters para nós.
public class TransacaoRequestDTO {

    private Long idContaOrigem;
    private String chavePixDestino;
    private BigDecimal valor;

}