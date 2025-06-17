package br.com.pixseguro.api.repository;

import java.util.Optional; // 1. Import necessário para o Optional

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pixseguro.api.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    
    // 2. Novo método "mágico"!
    // O Spring Data JPA entende o nome do método e cria a query automaticamente:
    // "SELECT c FROM Conta c WHERE c.chavePix = :chavePix"
    Optional<Conta> findByChavePix(String chavePix);
}