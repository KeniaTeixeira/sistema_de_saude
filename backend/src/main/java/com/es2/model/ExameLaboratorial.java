package com.es2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exames_laboratoriais")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExameLaboratorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    @Column(length = 500)
    private String descricao;

    private String psicologia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "atendimento_id")
    private Atendimento atendimento;
}