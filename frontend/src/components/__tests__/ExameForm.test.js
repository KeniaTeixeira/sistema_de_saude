import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { exameService, atendimentoService } from '../../services/api';
import ExameForm from '../ExameForm';

jest.mock('../../services/api');

const mockAtendimentos = [
  { id: 1, titulo: 'Consulta' },
  { id: 2, titulo: 'Retorno' },
];

function renderForm(path = '/exames/novo') {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <Routes>
        <Route path="/exames/novo" element={<ExameForm />} />
        <Route path="/exames/editar/:id" element={<ExameForm />} />
        <Route path="/exames" element={<div>Lista</div>} />
      </Routes>
    </MemoryRouter>
  );
}

describe('ExameForm', () => {
  beforeEach(() => {
    atendimentoService.listar.mockResolvedValue({ data: mockAtendimentos });
    exameService.criar.mockResolvedValue({ data: { id: 1 } });
    exameService.atualizar.mockResolvedValue({ data: {} });
    exameService.buscar.mockResolvedValue({
      data: { id: 1, descricao: 'Exame Antigo', psicologia: 'Psicologia', atendimento: { id: 1 } }
    });
  });

  it('deve renderizar formulário de novo exame', () => {
    renderForm();
    expect(screen.getByText('Novo Exame')).toBeInTheDocument();
    expect(screen.getByLabelText('Descrição')).toBeInTheDocument();
  });

  it('deve carregar atendimentos no select', async () => {
    renderForm();
    await waitFor(() => {
      expect(screen.getByText('Consulta')).toBeInTheDocument();
      expect(screen.getByText('Retorno')).toBeInTheDocument();
    });
  });

  it('deve chamar criar ao submeter formulário', async () => {
    renderForm();
    const user = userEvent.setup();

    await user.type(screen.getByLabelText('Descrição'), 'Novo Exame');
    await user.selectOptions(screen.getByLabelText('Atendimento'), '1');
    await user.click(screen.getByText('Salvar'));

    await waitFor(() => {
      expect(exameService.criar).toHaveBeenCalledWith(
        expect.objectContaining({ descricao: 'Novo Exame' })
      );
    });
  });

  it('deve carregar dados na edição', async () => {
    renderForm('/exames/editar/1');
    await waitFor(() => {
      expect(screen.getByDisplayValue('Exame Antigo')).toBeInTheDocument();
    });
  });
});
