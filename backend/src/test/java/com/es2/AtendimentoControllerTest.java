package com.es2;

import com.es2.controller.AtendimentoController;
import com.es2.model.Atendimento;
import com.es2.model.ProfissionalSaude;
import com.es2.repository.AtendimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AtendimentoController.class)
class AtendimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtendimentoRepository repository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveCriarAtendimentoComSucesso() throws Exception {

        Atendimento atendimento = new Atendimento();
        atendimento.setId(1L);
        atendimento.setTitulo("Consulta Online");
        atendimento.setData(LocalDate.of(2025, 6, 10));
        atendimento.setHorario(LocalTime.of(14, 0));

        when(repository.save(any(Atendimento.class)))
                .thenReturn(atendimento);

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atendimento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Consulta Online"));
    }

    @Test
    void deveListarAtendimentos() throws Exception {

        Atendimento a1 = new Atendimento();
        a1.setId(1L);
        a1.setTitulo("Consulta");

        Atendimento a2 = new Atendimento();
        a2.setId(2L);
        a2.setTitulo("Retorno");

        when(repository.findAllByOrderByDataAscHorarioAsc())
                .thenReturn(Arrays.asList(a1, a2));

        mockMvc.perform(get("/api/atendimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Consulta"))
                .andExpect(jsonPath("$[1].titulo").value("Retorno"));
    }

    @Test
    void deveRetornar404QuandoNaoEncontrar() throws Exception {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/atendimentos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarAtendimentoComSucesso() throws Exception {

        ProfissionalSaude p = new ProfissionalSaude();
        p.setId(1L);
        p.setNome("Dr. João");

        Atendimento existente = new Atendimento();
        existente.setId(1L);
        existente.setTitulo("Consulta");
        existente.setData(LocalDate.of(2025, 6, 10));
        existente.setHorario(LocalTime.of(14, 0));

        Atendimento dados = new Atendimento();
        dados.setTitulo("Consulta Atualizada");
        dados.setData(LocalDate.of(2025, 6, 15));
        dados.setHorario(LocalTime.of(15, 30));
        dados.setLinkVideoconferencia("https://meet.google.com/abc");
        dados.setReceita("Dipirona 500mg");
        dados.setProfissionalSaude(p);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Atendimento.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(put("/api/atendimentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Consulta Atualizada"))
                .andExpect(jsonPath("$.data").value("2025-06-15"))
                .andExpect(jsonPath("$.horario").value("15:30:00"));
    }

    @Test
    void deveDeletarAtendimentoComSucesso() throws Exception {

        Atendimento atendimento = new Atendimento();
        atendimento.setId(1L);
        atendimento.setTitulo("Consulta");

        when(repository.findById(1L)).thenReturn(Optional.of(atendimento));

        mockMvc.perform(delete("/api/atendimentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Atendimento removido com sucesso"));
    }

    @Test
    void deveRetornar404AoAtualizarAtendimentoInexistente() throws Exception {

        when(repository.findById(999L)).thenReturn(Optional.empty());

        Atendimento dados = new Atendimento();
        dados.setTitulo("Consulta");
        dados.setData(LocalDate.of(2025, 7, 1));
        dados.setHorario(LocalTime.of(10, 0));

        mockMvc.perform(put("/api/atendimentos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRejeitarCriacaoSemTitulo() throws Exception {

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Atendimento())))
                .andExpect(status().isBadRequest());
    }
}