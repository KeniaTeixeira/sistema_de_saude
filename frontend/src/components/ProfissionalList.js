import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalList() {

  const [profissionais, setProfissionais] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    carregarProfissionais();
  }, []);

  const carregarProfissionais = async () => {
    try {
      const response = await profissionalService.listar();
      setProfissionais(response.data);
    } catch (error) {
      console.error('Erro ao carregar profissionais:', error);
    } finally {
      setLoading(false);
    }
  };

  const deletarProfissional = async (id) => {
    if (window.confirm('Deseja realmente excluir este profissional?')) {
      try {
        await profissionalService.deletar(id);
        carregarProfissionais();
      } catch (error) {
        console.error('Erro ao excluir profissional:', error);
      }
    }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <div>

      <div className="header">
        <h2>Profissionais de Saúde</h2>

        <Link
          to="/profissionais/novo"
          className="btn btn-primary"
        >
          Novo Profissional
        </Link>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Categoria</th>
            <th>Email</th>
            <th>Telefone</th>
            <th>Ações</th>
          </tr>
        </thead>

        <tbody>

          {profissionais.map((prof) => (
            <tr key={prof.id}>
              <td>{prof.nome}</td>
              <td>{prof.categoria}</td>
              <td>{prof.email}</td>
              <td>{prof.telefone}</td>

              <td>

                <Link
                  to={`/profissionais/editar/${prof.id}`}
                  className="btn btn-sm"
                >
                  Editar
                </Link>

                <button
                  onClick={() => deletarProfissional(prof.id)}
                  className="btn btn-danger btn-sm"
                >
                  Excluir
                </button>

              </td>
            </tr>
          ))}

        </tbody>
      </table>

      {profissionais.length === 0 &&
        <p>Nenhum profissional cadastrado.</p>}
    </div>
  );
}

export default ProfissionalList;