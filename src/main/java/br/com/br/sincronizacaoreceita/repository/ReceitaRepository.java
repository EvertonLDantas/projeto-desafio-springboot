package br.com.br.sincronizacaoreceita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import br.com.br.sincronizacaoreceita.model.Receita;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    List<Receita> findByAgencia(String agencia);
}