import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { exameService, atendimentoService } from '../services/api';

function ExameForm() {

  const navigate = useNavigate();
  const { id } = useParams();

  const [atendimentos, setAtendimentos] = useState([]);

  const [exame, setExame] = useState({
    descricao: '',
    psicologia: '',
    atendimento: null
  });

  useEffect(() => {

    atendimentoService.listar()
      .then(response => setAtendimentos(response.data));

    if (id) {
      exameService.buscar(id)
        .then(response => setExame(response.data));
    }

  }, [id]);

  const handleSubmit = async (e) => {

    e.preventDefault();

    if (id) {
      await exameService.atualizar(id, exame);
    } else {
      await exameService.criar(exame);
    }

    navigate('/exames');
  };

  return (
    <div>

      <h2>
        {id ? 'Editar Exame' : 'Novo Exame'}
      </h2>

      <form onSubmit={handleSubmit}>

        <div className="form-group">

          <label>Descrição</label>

          <input
            type="text"
            value={exame.descricao}
            onChange={(e) =>
              setExame({
                ...exame,
                descricao: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">

          <label>Psicologia</label>

          <input
            type="text"
            value={exame.psicologia}
            onChange={(e) =>
              setExame({
                ...exame,
                psicologia: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">

          <label>Atendimento</label>

          <select
            value={exame.atendimento?.id || ''}
            onChange={(e) =>
              setExame({
                ...exame,
                atendimento: {
                  id: parseInt(e.target.value)
                }
              })
            }
          >

            <option value="">Selecione</option>

            {atendimentos.map((a) => (
              <option key={a.id} value={a.id}>
                {a.titulo}
              </option>
            ))}

          </select>

        </div>

        <button
          type="submit"
          className="btn btn-primary"
        >
          Salvar
        </button>

      </form>

    </div>
  );
}

export default ExameForm;