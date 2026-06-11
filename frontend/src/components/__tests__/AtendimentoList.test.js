import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import { atendimentoService } from '../../services/api';
import AtendimentoList from '../AtendimentoList';

jest.mock('../../services/api');

const mockAtendimentos = [
  { id: 1, titulo: 'Consulta', data: '2025-06-10', horario: '14:00:00', profissionalSaude: { nome: 'Dr. João' } },
  { id: 2, titulo: 'Retorno', data: '2025-06-15', horario: '10:30:00', profissionalSaude: { nome: 'Dra. Maria' } },
];

function renderComponent() {
  return render(
    <BrowserRouter>
      <AtendimentoList />
    </BrowserRouter>
  );
}

describe('AtendimentoList', () => {
  beforeEach(() => {
    atendimentoService.listar.mockResolvedValue({ data: mockAtendimentos });
    atendimentoService.deletar.mockResolvedValue({});
    window.confirm = jest.fn(() => true);
  });

  it('deve renderizar a lista de atendimentos', async () => {
    renderComponent();
    await waitFor(() => {
      expect(screen.getByText('Consulta')).toBeInTheDocument();
      expect(screen.getByText('Retorno')).toBeInTheDocument();
      expect(screen.getByText('Dr. João')).toBeInTheDocument();
      expect(screen.getByText('Dra. Maria')).toBeInTheDocument();
    });
  });

  it('deve chamar deletar ao clicar em excluir', async () => {
    renderComponent();
    await waitFor(() => screen.getByText('Consulta'));

    const botoes = screen.getAllByText('Excluir');
    await userEvent.click(botoes[0]);

    expect(atendimentoService.deletar).toHaveBeenCalledWith(1);
  });

  it('deve ter link para novo atendimento', async () => {
    renderComponent();
    await waitFor(() => {
      expect(screen.getByText('Novo Atendimento')).toHaveAttribute('href', '/atendimentos/novo');
    });
  });
});
