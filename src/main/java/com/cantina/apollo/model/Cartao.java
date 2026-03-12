package com.cantina.apollo.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeAluno;
    private String matricula;
    private BigDecimal saldoAtual;
    private BigDecimal limiteDiario;
    private String senha;
    private  BigDecimal totalGastoHoje = BigDecimal.ZERO;
    private LocalDate dataUltimaCompra = LocalDate.now();


}
