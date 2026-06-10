package com.es2;

import com.es2.controller.ProfissionalSaudeController;
import com.es2.model.ProfissionalSaude;
import com.es2.repository.ProfissionalSaudeRepository;
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

@WebMvcTest(ProfissionalSaudeController.class)
class ProfissionalSaudeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfissionalSaudeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarProfissionalComSucesso() throws Exception {

        ProfissionalSaude profissional = new ProfissionalSaude();
        profissional.setId(1L);
        profissional.setNome("Maria");
        profissional.setCategoria("Psicóloga");

        when(repository.save(any(ProfissionalSaude.class)))
                .thenReturn(profissional);

        mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profissional)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    void deveListarProfissionais() throws Exception {

        ProfissionalSaude p1 = new ProfissionalSaude();
        p1.setId(1L);
        p1.setNome("Maria");

        ProfissionalSaude p2 = new ProfissionalSaude();
        p2.setId(2L);
        p2.setNome("João");

        when(repository.findAll())
                .thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Maria"))
                .andExpect(jsonPath("$[1].nome").value("João"));
    }

    @Test
    void deveRetornar404ProfissionalInexistente() throws Exception {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profissionais/999"))
                .andExpect(status().isNotFound());
    }
}