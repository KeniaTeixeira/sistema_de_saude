import React from 'react';
import { render, screen } from '@testing-library/react';
import App from '../App';

jest.mock('../services/api');

describe('App', () => {
  it('deve renderizar o navbar com links de navegação', () => {
    render(<App />);
    expect(screen.getByText('Sistema de Saúde')).toBeInTheDocument();
    expect(screen.getByText('Profissionais')).toBeInTheDocument();
    expect(screen.getByText('Atendimentos')).toBeInTheDocument();
    expect(screen.getByText('Exames')).toBeInTheDocument();
  });

  it('deve ter links de navegação corretos', () => {
    render(<App />);
    expect(screen.getByText('Profissionais').closest('a')).toHaveAttribute('href', '/profissionais');
    expect(screen.getByText('Atendimentos').closest('a')).toHaveAttribute('href', '/atendimentos');
    expect(screen.getByText('Exames').closest('a')).toHaveAttribute('href', '/exames');
  });
});
