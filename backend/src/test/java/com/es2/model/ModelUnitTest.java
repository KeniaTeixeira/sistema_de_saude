package com.es2.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class ModelUnitTest {

    @Test
    void deveCriarProfissionalSaudeViaConstrutor() {
        ProfissionalSaude p = new ProfissionalSaude(1L, "Ana", "Rua X", "ana@teste.com", "11999999999", "Médico");
        assertEquals(1L, p.getId());
        assertEquals("Ana", p.getNome());
        assertEquals("Médico", p.getCategoria());
    }

    @Test
    void deveAlterarDadosDoProfissional() {
        ProfissionalSaude p = new ProfissionalSaude();
        p.setNome("Carlos");
        p.setCategoria("Fisioterapeuta");
        p.setTelefone("11988888888");
        assertEquals("Carlos", p.getNome());
        assertEquals("Fisioterapeuta", p.getCategoria());
        assertEquals("11988888888", p.getTelefone());
    }

    @Test
    void deveCriarAtendimentoViaConstrutor() {
        ProfissionalSaude p = new ProfissionalSaude(1L, "Ana", null, null, null, null);
        Atendimento a = new Atendimento(1L, LocalDate.of(2025, 6, 10), LocalTime.of(14, 0), "Consulta", "https://meet.google.com", "Receita", p);

        assertEquals("Consulta", a.getTitulo());
        assertEquals(LocalDate.of(2025, 6, 10), a.getData());
        assertEquals(LocalTime.of(14, 0), a.getHorario());
        assertEquals(p, a.getProfissionalSaude());
    }

    @Test
    void deveAlterarDadosDoAtendimento() {
        Atendimento a = new Atendimento();
        a.setTitulo("Retorno");
        a.setReceita("Dipirona");
        a.setLinkVideoconferencia("https://zoom.us/teste");

        assertEquals("Retorno", a.getTitulo());
        assertEquals("Dipirona", a.getReceita());
        assertEquals("https://zoom.us/teste", a.getLinkVideoconferencia());
    }

    @Test
    void deveCriarExameLaboratorialViaConstrutor() {
        Atendimento a = new Atendimento(1L, null, null, "Consulta", null, null, null);
        ExameLaboratorial e = new ExameLaboratorial(1L, "Exame de Sangue", "Psicologia", a);

        assertEquals("Exame de Sangue", e.getDescricao());
        assertEquals("Psicologia", e.getPsicologia());
        assertEquals(a, e.getAtendimento());
    }

    @Test
    void deveAlterarDadosDoExame() {
        ExameLaboratorial e = new ExameLaboratorial();
        e.setDescricao("Raio-X");
        e.setPsicologia("Teste Cognitivo");

        assertEquals("Raio-X", e.getDescricao());
        assertEquals("Teste Cognitivo", e.getPsicologia());
    }

    @Test
    void deveCompararIgualdadeProfissional() {
        ProfissionalSaude p1 = new ProfissionalSaude(1L, "Ana", null, null, null, null);
        ProfissionalSaude p2 = new ProfissionalSaude(1L, "Ana", null, null, null, null);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
