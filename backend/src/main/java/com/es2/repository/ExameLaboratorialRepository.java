package com.es2.repository;

import com.es2.model.ExameLaboratorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExameLaboratorialRepository extends JpaRepository<ExameLaboratorial, Long> {

    List<ExameLaboratorial> findByAtendimentoId(Long atendimentoId);
}