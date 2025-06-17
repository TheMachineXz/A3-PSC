package br.com.pixseguro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pixseguro.api.model.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    // Por enquanto, fica vazio. A herança do JpaRepository já nos dá tudo o que precisamos.
}