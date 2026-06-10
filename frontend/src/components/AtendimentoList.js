import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { atendimentoService } from '../services/api';

function AtendimentoList() {

  const [atendimentos, setAtendimentos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    carregarAtendimentos();
  }, []);

  const carregarAtendimentos = async () => {
    try {
      const response = await atendimentoService.listar();
      setAtendimentos(response.data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const deletarAtendimento = async (id) => {
    if (window.confirm('Deseja excluir este atendimento?')) {
      await atendimentoService.deletar(id);
      carregarAtendimentos();
    }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <div>

      <div className="header">
        <h2>Atendimentos</h2>

        <Link
          to="/atendimentos/novo"
          className="btn btn-primary"
        >
          Novo Atendimento
        </Link>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>Título</th>
            <th>Data</th>
            <th>Horário</th>
            <th>Profissional</th>
            <th>Ações</th>
          </tr>
        </thead>

        <tbody>
          {atendimentos.map((atendimento) => (
            <tr key={atendimento.id}>
              <td>{atendimento.titulo}</td>
              <td>{atendimento.data}</td>
              <td>{atendimento.horario}</td>
              <td>{atendimento.profissionalSaude?.nome}</td>

              <td>

                <Link
                  to={`/atendimentos/editar/${atendimento.id}`}
                  className="btn btn-sm"
                >
                  Editar
                </Link>

                <button
                  onClick={() => deletarAtendimento(atendimento.id)}
                  className="btn btn-danger btn-sm"
                >
                  Excluir
                </button>

              </td>
            </tr>
          ))}
        </tbody>
      </table>

    </div>
  );
}

export default AtendimentoList;