package com.es2.controller;

import com.es2.model.ExameLaboratorial;
import com.es2.repository.ExameLaboratorialRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exames")
@CrossOrigin(origins = "*")
public class ExameLaboratorialController {

    private final ExameLaboratorialRepository repository;

    public ExameLaboratorialController(ExameLaboratorialRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<ExameLaboratorial> criar(
            @Valid @RequestBody ExameLaboratorial exame) {

        ExameLaboratorial salvo = repository.save(exame);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<ExameLaboratorial>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ExameLaboratorial dados) {

        return repository.findById(id)
                .map(exame -> {

                    exame.setDescricao(dados.getDescricao());
                    exame.setPsicologia(dados.getPsicologia());
                    exame.setAtendimento(dados.getAtendimento());

                    return ResponseEntity.ok(repository.save(exame));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {

        return repository.findById(id)
                .map(exame -> {

                    repository.delete(exame);

                    return ResponseEntity.ok(
                            Map.of("mensagem",
                                    "Exame removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}