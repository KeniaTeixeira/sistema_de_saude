package com.es2;

import com.es2.controller.ExameLaboratorialController;
import com.es2.model.Atendimento;
import com.es2.model.ExameLaboratorial;
import com.es2.repository.ExameLaboratorialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExameLaboratorialController.class)
class ExameLaboratorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExameLaboratorialRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarExameComSucesso() throws Exception {

        ExameLaboratorial exame = new ExameLaboratorial();
        exame.setId(1L);
        exame.setDescricao("Exame Psicológico");

        when(repository.save(any(ExameLaboratorial.class)))
                .thenReturn(exame);

        mockMvc.perform(post("/api/exames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exame)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao")
                        .value("Exame Psicológico"));
    }

    @Test
    void deveListarExames() throws Exception {

        ExameLaboratorial e1 = new ExameLaboratorial();
        e1.setId(1L);
        e1.setDescricao("Exame A");

        ExameLaboratorial e2 = new ExameLaboratorial();
        e2.setId(2L);
        e2.setDescricao("Exame B");

        when(repository.findAll())
                .thenReturn(Arrays.asList(e1, e2));

        mockMvc.perform(get("/api/exames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Exame A"))
                .andExpect(jsonPath("$[1].descricao").value("Exame B"));
    }

    @Test
    void deveRetornar404ExameInexistente() throws Exception {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/exames/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarExameComSucesso() throws Exception {

        Atendimento a = new Atendimento();
        a.setId(1L);
        a.setTitulo("Consulta");

        ExameLaboratorial existente = new ExameLaboratorial();
        existente.setId(1L);
        existente.setDescricao("Exame Antigo");

        ExameLaboratorial dados = new ExameLaboratorial();
        dados.setDescricao("Exame Novo");
        dados.setPsicologia("Teste Cognitivo");
        dados.setAtendimento(a);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(ExameLaboratorial.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(put("/api/exames/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Exame Novo"))
                .andExpect(jsonPath("$.psicologia").value("Teste Cognitivo"));
    }

    @Test
    void deveDeletarExameComSucesso() throws Exception {

        ExameLaboratorial exame = new ExameLaboratorial();
        exame.setId(1L);
        exame.setDescricao("Exame");

        when(repository.findById(1L)).thenReturn(Optional.of(exame));

        mockMvc.perform(delete("/api/exames/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Exame removido com sucesso"));
    }

    @Test
    void deveRetornar404AoAtualizarExameInexistente() throws Exception {

        when(repository.findById(999L)).thenReturn(Optional.empty());

        ExameLaboratorial dados = new ExameLaboratorial();
        dados.setDescricao("Exame");

        mockMvc.perform(put("/api/exames/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRejeitarCriacaoSemDescricao() throws Exception {

        mockMvc.perform(post("/api/exames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ExameLaboratorial())))
                .andExpect(status().isBadRequest());
    }
}