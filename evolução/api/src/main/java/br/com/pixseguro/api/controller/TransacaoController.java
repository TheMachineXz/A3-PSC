package br.com.pixseguro.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; // 1. Import necessário
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pixseguro.api.dto.TransacaoRequestDTO;
import br.com.pixseguro.api.service.TransacaoService;

@RestController
@RequestMapping("/transacoes")
@CrossOrigin(origins = "*") // 2. Permite pedidos de qualquer origem.
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<Void> realizarTransacao(@RequestBody TransacaoRequestDTO dto) {
        try {
            transacaoService.realizarTransacao(
                dto.getIdContaOrigem(), 
                dto.getChavePixDestino(), 
                dto.getValor()
            );
            
            return ResponseEntity.ok().build();

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().header("X-Error-Message", e.getMessage()).build();
        }
    }
}