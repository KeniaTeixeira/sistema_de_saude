import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { profissionalService } from '../../services/api';
import ProfissionalForm from '../ProfissionalForm';

jest.mock('../../services/api');

function renderForm(path = '/profissionais/novo') {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <Routes>
        <Route path="/profissionais/novo" element={<ProfissionalForm />} />
        <Route path="/profissionais/editar/:id" element={<ProfissionalForm />} />
        <Route path="/profissionais" element={<div>Lista</div>} />
      </Routes>
    </MemoryRouter>
  );
}

describe('ProfissionalForm', () => {
  beforeEach(() => {
    profissionalService.criar.mockResolvedValue({ data: { id: 1 } });
    profissionalService.atualizar.mockResolvedValue({ data: {} });
    profissionalService.buscar.mockResolvedValue({
      data: { id: 1, nome: 'Maria', endereco: 'Rua A', email: 'maria@teste.com', telefone: '11999999999', categoria: 'Psicóloga' }
    });
  });

  it('deve renderizar formulário de novo profissional', () => {
    renderForm();
    expect(screen.getByText('Novo Profissional')).toBeInTheDocument();
    expect(screen.getByLabelText('Nome *')).toBeInTheDocument();
    expect(screen.getByLabelText('Categoria *')).toBeInTheDocument();
  });

  it('deve chamar criar ao submeter formulário', async () => {
    renderForm();
    const user = userEvent.setup();

    await user.type(screen.getByLabelText('Nome *'), 'Novo Prof');
    await user.selectOptions(screen.getByLabelText('Categoria *'), 'Médico');
    await user.click(screen.getByText('Salvar'));

    await waitFor(() => {
      expect(profissionalService.criar).toHaveBeenCalledWith(
        expect.objectContaining({ nome: 'Novo Prof', categoria: 'Médico' })
      );
    });
  });

  it('deve carregar dados do profissional na edição', async () => {
    renderForm('/profissionais/editar/1');
    await waitFor(() => {
      expect(screen.getByText('Editar Profissional')).toBeInTheDocument();
      expect(screen.getByLabelText('Nome *')).toHaveValue('Maria');
    });
  });

  it('deve chamar atualizar ao editar', async () => {
    renderForm('/profissionais/editar/1');
    await waitFor(() => screen.getByDisplayValue('Maria'));

    const user = userEvent.setup();
    await user.clear(screen.getByLabelText('Nome *'));
    await user.type(screen.getByLabelText('Nome *'), 'Maria Updated');
    await user.click(screen.getByText('Salvar'));

    await waitFor(() => {
      expect(profissionalService.atualizar).toHaveBeenCalledWith('1', expect.objectContaining({ nome: 'Maria Updated' }));
    });
  });
});
