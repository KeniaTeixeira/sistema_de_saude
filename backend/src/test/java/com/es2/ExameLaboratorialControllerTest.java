package com.es2;

import com.es2.controller.ExameLaboratorialController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}