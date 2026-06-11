import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { atendimentoService, profissionalService } from '../services/api';

function AtendimentoForm() {

  const navigate = useNavigate();
  const { id } = useParams();

  const [atendimento, setAtendimento] = useState({
    titulo: '',
    data: '',
    horario: '',
    linkVideoconferencia: '',
    receita: '',
    profissionalSaude: null
  });

  const [profissionais, setProfissionais] = useState([]);

  useEffect(() => {

    profissionalService.listar()
      .then(response => setProfissionais(response.data));

    if (id) {
      atendimentoService.buscar(id)
        .then(response => setAtendimento(response.data));
    }

  }, [id]);

  const handleSubmit = async (e) => {

    e.preventDefault();

    try {

      if (id) {
        await atendimentoService.atualizar(id, atendimento);
      } else {
        await atendimentoService.criar(atendimento);
      }

      navigate('/atendimentos');

    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div>

      <h2>
        {id ? 'Editar Atendimento' : 'Novo Atendimento'}
      </h2>

      <form onSubmit={handleSubmit} className="form">

        <div className="form-group">
          <label htmlFor="titulo">Título *</label>

          <input
            id="titulo"
            type="text"
            required
            value={atendimento.titulo}
            onChange={(e) =>
              setAtendimento({
                ...atendimento,
                titulo: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">
          <label htmlFor="data">Data *</label>

          <input
            id="data"
            type="date"
            required
            value={atendimento.data}
            onChange={(e) =>
              setAtendimento({
                ...atendimento,
                data: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">
          <label htmlFor="horario">Horário *</label>

          <input
            id="horario"
            type="time"
            required
            value={atendimento.horario}
            onChange={(e) =>
              setAtendimento({
                ...atendimento,
                horario: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">
          <label htmlFor="linkVideoconferencia">Link Videoconferência</label>

          <input
            id="linkVideoconferencia"
            type="text"
            value={atendimento.linkVideoconferencia}
            onChange={(e) =>
              setAtendimento({
                ...atendimento,
                linkVideoconferencia: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">
          <label htmlFor="receita">Receita</label>

          <textarea
            id="receita"
            value={atendimento.receita}
            onChange={(e) =>
              setAtendimento({
                ...atendimento,
                receita: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">
          <label htmlFor="profissional">Profissional</label>

          <select
            id="profissional"
            value={atendimento.profissionalSaude?.id || ''}
            onChange={(e) =>
              setAtendimento({
                ...atendimento,
                profissionalSaude: {
                  id: parseInt(e.target.value)
                }
              })
            }
          >
            <option value="">Selecione</option>

            {profissionais.map((prof) => (
              <option key={prof.id} value={prof.id}>
                {prof.nome}
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

export default AtendimentoForm;