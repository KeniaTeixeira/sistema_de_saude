package com.es2;

import com.es2.model.ProfissionalSaude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveExecutarFluxoCompletoProfissionalSaude() throws Exception {

        ProfissionalSaude profissional = new ProfissionalSaude();

        profissional.setNome("Sabrina Souza");
        profissional.setEndereco("Rua A");
        profissional.setTelefone("31999999999");
        profissional.setCategoria("Psicóloga");

        MvcResult result = mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profissional)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Sabrina Souza"))
                .andReturn();

        Long id = objectMapper.readTree(
                result.getResponse().getContentAsString())
                .get("id").asLong();

        mockMvc.perform(get("/api/profissionais/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria").value("Psicóloga"));

        profissional.setNome("Sabrina Souza Silva");

        mockMvc.perform(put("/api/profissionais/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profissional)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome")
                        .value("Sabrina Souza Silva"));

        mockMvc.perform(delete("/api/profissionais/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void deveVincularAtendimentoAProfissional() throws Exception {

        String profissionalJson = """
        {
          "nome":"Carlos Mendes",
          "endereco":"Rua B",
          "telefone":"31988888888",
          "categoria":"Médico"
        }
        """;

        MvcResult profissionalResult = mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(profissionalJson))
                .andExpect(status().isCreated())
                .andReturn();

        Long profissionalId = objectMapper.readTree(
                profissionalResult.getResponse().getContentAsString())
                .get("id").asLong();

        String atendimentoJson = String.format("""
        {
            "titulo":"Consulta Clínica",
            "data":"2025-06-20",
            "horario":"14:00:00",
            "linkVideoconferencia":"https://meet.google.com/teste",
            "receita":"Paracetamol",
            "profissionalSaude":{
                "id":%d
            }
        }
        """, profissionalId);

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(atendimentoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo")
                        .value("Consulta Clínica"));
    }

    @Test
    void deveVincularExameAoAtendimento() throws Exception {

        String profissionalJson = """
        {
          "nome":"Fernanda",
          "endereco":"Rua C",
          "telefone":"31977777777",
          "categoria":"Psicóloga"
        }
        """;

        MvcResult profissionalResult = mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(profissionalJson))
                .andExpect(status().isCreated())
                .andReturn();

        Long profissionalId = objectMapper.readTree(
                profissionalResult.getResponse().getContentAsString())
                .get("id").asLong();

        String atendimentoJson = String.format("""
        {
            "titulo":"Sessão Terapêutica",
            "data":"2025-06-21",
            "horario":"10:00:00",
            "receita":"Atividade Mental",
            "profissionalSaude":{
                "id":%d
            }
        }
        """, profissionalId);

        MvcResult atendimentoResult = mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(atendimentoJson))
                .andExpect(status().isCreated())
                .andReturn();

        Long atendimentoId = objectMapper.readTree(
                atendimentoResult.getResponse().getContentAsString())
                .get("id").asLong();

        String exameJson = String.format("""
        {
            "descricao":"Avaliação Psicológica",
            "psicologia":"Teste Cognitivo",
            "atendimento":{
                "id":%d
            }
        }
        """, atendimentoId);

        mockMvc.perform(post("/api/exames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(exameJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao")
                        .value("Avaliação Psicológica"));
    }
}