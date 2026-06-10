package com.es2.repository;

import com.es2.model.ProfissionalSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, Long> {

    List<ProfissionalSaude> findByNomeContainingIgnoreCase(String nome);

    List<ProfissionalSaude> findByCategoriaIgnoreCase(String categoria);
}