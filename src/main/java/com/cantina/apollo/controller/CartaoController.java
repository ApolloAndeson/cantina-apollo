    package com.cantina.apollo.controller;

    import com.cantina.apollo.model.Cartao;
    import com.cantina.apollo.repository.CartaoRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.math.BigDecimal;
    import java.util.List;
    import java.util.Optional;

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
            cartao.setSaldoAtual(cartao.getSaldoAtual().subtract(valor));
            return repository.save(cartao);

        }
        @DeleteMapping("/{id}")
        public void apagarCartao(@PathVariable Long id) {
            repository.deleteById(id);
        }
        @PutMapping("/{id}/recarregar")
        public Cartao recargaCartao(@PathVariable Long id, @RequestParam BigDecimal valor, @RequestParam String senha) {
            Cartao cartao = repository.findById(id).get();
            if (cartao.getSenha().equals(senha)) {
                cartao.setSaldoAtual(cartao.getSaldoAtual().add(valor));
                return repository.save(cartao);
            } else {
                throw new RuntimeException("Senha incorreta! Tente novamente.");
            }
            }
    }
