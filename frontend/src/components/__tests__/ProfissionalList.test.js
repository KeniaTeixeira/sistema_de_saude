import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import { profissionalService } from '../../services/api';
import ProfissionalList from '../ProfissionalList';

jest.mock('../../services/api');

const mockProfissionais = [
  { id: 1, nome: 'Maria', categoria: 'Psicóloga', email: 'maria@teste.com', telefone: '11999999999' },
  { id: 2, nome: 'João', categoria: 'Médico', email: 'joao@teste.com', telefone: '11988888888' },
];

function renderComponent() {
  return render(
    <BrowserRouter>
      <ProfissionalList />
    </BrowserRouter>
  );
}

describe('ProfissionalList', () => {
  beforeEach(() => {
    profissionalService.listar.mockResolvedValue({ data: mockProfissionais });
    profissionalService.deletar.mockResolvedValue({});
    window.confirm = jest.fn(() => true);
  });

  it('deve renderizar a lista de profissionais', async () => {
    renderComponent();
    expect(screen.getByText('Carregando...')).toBeInTheDocument();
    await waitFor(() => {
      expect(screen.getByText('Maria')).toBeInTheDocument();
      expect(screen.getByText('João')).toBeInTheDocument();
    });
  });

  it('deve chamar deletar ao clicar em excluir', async () => {
    renderComponent();
    await waitFor(() => screen.getByText('Maria'));

    const botoesExcluir = screen.getAllByText('Excluir');
    await userEvent.click(botoesExcluir[0]);

    expect(window.confirm).toHaveBeenCalled();
    expect(profissionalService.deletar).toHaveBeenCalledWith(1);
  });

  it('deve exibir mensagem quando lista vazia', async () => {
    profissionalService.listar.mockResolvedValue({ data: [] });
    renderComponent();
    await waitFor(() => {
      expect(screen.getByText('Nenhum profissional cadastrado.')).toBeInTheDocument();
    });
  });

  it('deve ter link para novo profissional', async () => {
    renderComponent();
    await waitFor(() => {
      expect(screen.getByText('Novo Profissional')).toHaveAttribute('href', '/profissionais/novo');
    });
  });
});
