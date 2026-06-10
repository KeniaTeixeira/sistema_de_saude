package com.es2.controller;

import com.es2.model.ProfissionalSaude;
import com.es2.repository.ProfissionalSaudeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
public class ProfissionalSaudeController {

    private final ProfissionalSaudeRepository repository;

    public ProfissionalSaudeController(ProfissionalSaudeRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<ProfissionalSaude> criar(
            @Valid @RequestBody ProfissionalSaude profissional) {

        ProfissionalSaude salvo = repository.save(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalSaude>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public List<ProfissionalSaude> buscarPorNome(@PathVariable String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    @GetMapping("/categoria/{categoria}")
    public List<ProfissionalSaude> buscarPorCategoria(@PathVariable String categoria) {
        return repository.findByCategoriaIgnoreCase(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfissionalSaude dados) {

        return repository.findById(id)
                .map(profissional -> {

                    profissional.setNome(dados.getNome());
                    profissional.setEndereco(dados.getEndereco());
                    profissional.setTelefone(dados.getTelefone());
                    profissional.setCategoria(dados.getCategoria());

                    return ResponseEntity.ok(repository.save(profissional));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        return repository.findById(id)
                .map(profissional -> {

                    repository.delete(profissional);

                    return ResponseEntity.ok(
                            Map.of("mensagem",
                                    "Profissional removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}