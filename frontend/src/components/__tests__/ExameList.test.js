import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import { exameService } from '../../services/api';
import ExameList from '../ExameList';

jest.mock('../../services/api');

const mockExames = [
  { id: 1, descricao: 'Exame de Sangue', psicologia: 'Teste Cognitivo', atendimento: { titulo: 'Consulta' } },
  { id: 2, descricao: 'Raio-X', psicologia: '', atendimento: { titulo: 'Retorno' } },
];

function renderComponent() {
  return render(
    <BrowserRouter>
      <ExameList />
    </BrowserRouter>
  );
}

describe('ExameList', () => {
  beforeEach(() => {
    exameService.listar.mockResolvedValue({ data: mockExames });
    exameService.deletar.mockResolvedValue({});
  });

  it('deve renderizar a lista de exames', async () => {
    renderComponent();
    await waitFor(() => {
      expect(screen.getByText('Exame de Sangue')).toBeInTheDocument();
      expect(screen.getByText('Raio-X')).toBeInTheDocument();
      expect(screen.getByText('Consulta')).toBeInTheDocument();
      expect(screen.getByText('Retorno')).toBeInTheDocument();
    });
  });

  it('deve chamar deletar ao clicar em excluir', async () => {
    renderComponent();
    await waitFor(() => screen.getByText('Exame de Sangue'));

    const botoes = screen.getAllByText('Excluir');
    await userEvent.click(botoes[0]);

    expect(exameService.deletar).toHaveBeenCalledWith(1);
  });

  it('deve ter link para novo exame', () => {
    renderComponent();
    expect(screen.getByText('Novo Exame')).toHaveAttribute('href', '/exames/novo');
  });
});
