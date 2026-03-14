package com.cantina.apollo.controller;

import com.cantina.apollo.model.Cartao;
import com.cantina.apollo.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
    @Autowired
    private CartaoRepository repository;

    @PostMapping
    public Cartao emitirCartao(@RequestBody Cartao novoCartao) {
        return repository.save(novoCartao);

    }

    @GetMapping
    public List<Cartao> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Cartao> buscarPorId(@PathVariable Long id) {
        return repository.findById(id);

    }

    @PutMapping("/{id}/comprar")
    public Cartao comprarLanche(@PathVariable Long id, @RequestParam BigDecimal valor) {
        Cartao cartao = repository.findById(id).get();
        LocalDate dataHoje = LocalDate.now();

        if (!cartao.getDataUltimaCompra().equals(dataHoje)) {
            cartao.setTotalGastoHoje(BigDecimal.ZERO);
            cartao.setDataUltimaCompra(dataHoje);

        }
        BigDecimal previsaoGastos = cartao.getTotalGastoHoje().add(valor);

        if (previsaoGastos.compareTo(cartao.getLimiteDiario()) > 0) {
            throw new RuntimeException("Compra negada! Limite diário excedido.");

        }
        cartao.setTotalGastoHoje(previsaoGastos);

        cartao.setSaldoAtual(cartao.getSaldoAtual().subtract(valor));
        return repository.save(cartao);

    }

    @DeleteMapping("/{id}")
    public void apagarCartao(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("/{id}/recarregar")
    public Cartao recargaCartao(@PathVariable Long id, @RequestBody Map<String, Object> dados) {
        BigDecimal valor = new BigDecimal(dados.get("valor").toString());
        String senha = (String) dados.get("senha");

        Cartao cartao = repository.findById(id).get();
        if (cartao.getSenha().equals(senha)) {
            cartao.setSaldoAtual(cartao.getSaldoAtual().add(valor));
            return repository.save(cartao);
        } else {
            throw new RuntimeException("Senha incorreta! Tente novamente.");
        }
    }

    @PostMapping("/login")
    public Cartao login(@RequestBody Map<String, String> dados) {
        String matricula = dados.get("matricula");
        String senha = dados.get("senha");

        Optional<Cartao> busca = repository.findByMatricula(matricula);
        if (busca.isEmpty()) {
            throw new RuntimeException("Matrícula não encontrada!");
        }
        Cartao cartao = busca.get();
        if (cartao.getSenha().equals(senha)) {
            return cartao;

        } else {
            throw new RuntimeException("Senha incorreta!");
        }

    }
    @GetMapping("/perfil/{matricula}")
    public Cartao buscarPorMatricula(@PathVariable String matricula) {
        Optional<Cartao> busca = repository.findByMatricula(matricula);

        if(busca.isEmpty()) {
            throw new RuntimeException("Matricula não existente");
        }
        Cartao cartao = busca.get();

        return cartao;

    }
}
