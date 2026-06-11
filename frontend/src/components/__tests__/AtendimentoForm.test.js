import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { atendimentoService, profissionalService } from '../../services/api';
import AtendimentoForm from '../AtendimentoForm';

jest.mock('../../services/api');

const mockProfissionais = [
  { id: 1, nome: 'Dr. João' },
  { id: 2, nome: 'Dra. Maria' },
];

function renderForm(path = '/atendimentos/novo') {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <Routes>
        <Route path="/atendimentos/novo" element={<AtendimentoForm />} />
        <Route path="/atendimentos/editar/:id" element={<AtendimentoForm />} />
        <Route path="/atendimentos" element={<div>Lista</div>} />
      </Routes>
    </MemoryRouter>
  );
}

describe('AtendimentoForm', () => {
  beforeEach(() => {
    profissionalService.listar.mockResolvedValue({ data: mockProfissionais });
    atendimentoService.criar.mockResolvedValue({ data: { id: 1 } });
    atendimentoService.atualizar.mockResolvedValue({ data: {} });
    atendimentoService.buscar.mockResolvedValue({
      data: { id: 1, titulo: 'Consulta', data: '2025-06-10', horario: '14:00', linkVideoconferencia: '', receita: '', profissionalSaude: { id: 1 } }
    });
  });

  it('deve renderizar formulário com campos obrigatórios', () => {
    renderForm();
    expect(screen.getByText('Novo Atendimento')).toBeInTheDocument();
    expect(screen.getByLabelText('Título *')).toBeInTheDocument();
    expect(screen.getByLabelText('Data *')).toBeInTheDocument();
    expect(screen.getByLabelText('Horário *')).toBeInTheDocument();
  });

  it('deve carregar profissionais no select', async () => {
    renderForm();
    await waitFor(() => {
      expect(screen.getByText('Dr. João')).toBeInTheDocument();
      expect(screen.getByText('Dra. Maria')).toBeInTheDocument();
    });
  });

  it('deve chamar criar ao submeter formulário', async () => {
    renderForm();
    const user = userEvent.setup();

    await user.type(screen.getByLabelText('Título *'), 'Nova Consulta');
    await user.type(screen.getByLabelText('Data *'), '2025-07-01');
    await user.type(screen.getByLabelText('Horário *'), '09:00');
    await user.selectOptions(screen.getByLabelText('Profissional'), '1');
    await user.click(screen.getByText('Salvar'));

    await waitFor(() => {
      expect(atendimentoService.criar).toHaveBeenCalled();
    });
  });

  it('deve carregar dados do atendimento na edição', async () => {
    renderForm('/atendimentos/editar/1');
    await waitFor(() => {
      expect(screen.getByDisplayValue('Consulta')).toBeInTheDocument();
    });
  });
});
