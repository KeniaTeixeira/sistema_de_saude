import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { exameService } from '../services/api';

function ExameList() {

  const [exames, setExames] = useState([]);

  useEffect(() => {
    carregarExames();
  }, []);

  const carregarExames = async () => {
    const response = await exameService.listar();
    setExames(response.data);
  };

  const deletar = async (id) => {
    await exameService.deletar(id);
    carregarExames();
  };

  return (
    <div>

      <div className="header">
        <h2>Exames</h2>

        <Link
          to="/exames/novo"
          className="btn btn-primary"
        >
          Novo Exame
        </Link>
      </div>

      <table className="table">

        <thead>
          <tr>
            <th>Descrição</th>
            <th>Psicologia</th>
            <th>Atendimento</th>
            <th>Ações</th>
          </tr>
        </thead>

        <tbody>

          {exames.map((exame) => (
            <tr key={exame.id}>

              <td>{exame.descricao}</td>
              <td>{exame.psicologia}</td>
              <td>{exame.atendimento?.titulo}</td>

              <td>

                <Link
                  to={`/exames/editar/${exame.id}`}
                  className="btn btn-sm"
                >
                  Editar
                </Link>

                <button
                  className="btn btn-danger btn-sm"
                  onClick={() => deletar(exame.id)}
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

export default ExameList;