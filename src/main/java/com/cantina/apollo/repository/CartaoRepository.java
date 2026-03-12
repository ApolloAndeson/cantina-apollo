package com.cantina.apollo.repository;

import com.cantina.apollo.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    Optional<Cartao> findByMatricula(String matricula);

}
