package br.com.pixseguro.api.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; // 1. Import necessário
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pixseguro.api.model.Conta;
import br.com.pixseguro.api.service.ContaService;

@RestController
@RequestMapping("/contas")
@CrossOrigin(origins = "*") // 2. Permite pedidos de qualquer origem.
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<Conta> criar(@RequestBody Conta conta) {
        Conta contaSalva = contaService.criarConta(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaSalva);
    }

    @PatchMapping("/{id}/depositar")
    public ResponseEntity<?> depositar(@PathVariable Long id, @RequestBody BigDecimal valor) {
        try {
            Conta contaAtualizada = contaService.adicionarSaldo(id, valor);
            return ResponseEntity.ok(contaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().header("X-Error-Message", e.getMessage()).build();
        }
    }
}