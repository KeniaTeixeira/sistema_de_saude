import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalForm() {

  const navigate = useNavigate();
  const { id } = useParams();

  const [profissional, setProfissional] = useState({
    nome: '',
    endereco: '',
    email: '',
    telefone: '',
    categoria: ''
  });

  useEffect(() => {
    if (id) {
      profissionalService.buscar(id)
        .then(response => setProfissional(response.data));
    }
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {

      if (id) {
        await profissionalService.atualizar(id, profissional);
      } else {
        await profissionalService.criar(profissional);
      }

      navigate('/profissionais');

    } catch (error) {
      console.error('Erro ao salvar profissional:', error);
    }
  };

  return (
    <div>

      <h2>
        {id ? 'Editar Profissional' : 'Novo Profissional'}
      </h2>

      <form onSubmit={handleSubmit} className="form">

        <div className="form-group">
          <label>Nome *</label>

          <input
            type="text"
            required
            value={profissional.nome}
            onChange={(e) =>
              setProfissional({
                ...profissional,
                nome: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">
          <label>Endereço</label>

          <input
            type="text"
            value={profissional.endereco}
            onChange={(e) =>
              setProfissional({
                ...profissional,
                endereco: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">
          <label>Email</label>

          <input
            type="email"
            value={profissional.email}
            onChange={(e) =>
              setProfissional({
                ...profissional,
                email: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">
          <label>Telefone</label>

          <input
            type="text"
            value={profissional.telefone}
            onChange={(e) =>
              setProfissional({
                ...profissional,
                telefone: e.target.value
              })
            }
          />
        </div>

        <div className="form-group">
          <label>Categoria *</label>

          <select
            required
            value={profissional.categoria}
            onChange={(e) =>
              setProfissional({
                ...profissional,
                categoria: e.target.value
              })
            }
          >
            <option value="">Selecione</option>
            <option value="Médico">Médico</option>
            <option value="Psicóloga">Psicóloga</option>
            <option value="Fisioterapeuta">Fisioterapeuta</option>
          </select>
        </div>

        <button
          type="submit"
          className="btn btn-primary"
        >
          Salvar
        </button>

        <button
          type="button"
          className="btn"
          onClick={() => navigate('/profissionais')}
        >
          Cancelar
        </button>

      </form>
    </div>
  );
}

export default ProfissionalForm;