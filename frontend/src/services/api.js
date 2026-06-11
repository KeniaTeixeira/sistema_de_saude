import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' }
});

export const profissionalService = {
  listar: () => api.get('/profissionais'),
  buscar: (id) => api.get(`/profissionais/${id}`),
  criar: (dados) => api.post('/profissionais', dados),
  atualizar: (id, dados) => api.put(`/profissionais/${id}`, dados),
  deletar: (id) => api.delete(`/profissionais/${id}`)
};

export const atendimentoService = {
  listar: () => api.get('/atendimentos'),
  buscar: (id) => api.get(`/atendimentos/${id}`),
  criar: (dados) => api.post('/atendimentos', dados),
  atualizar: (id, dados) => api.put(`/atendimentos/${id}`, dados),
  deletar: (id) => api.delete(`/atendimentos/${id}`)
};

export const exameService = {
  listar: () => api.get('/exames'),
  buscar: (id) => api.get(`/exames/${id}`),
  criar: (dados) => api.post('/exames', dados),
  atualizar: (id, dados) => api.put(`/exames/${id}`, dados),
  deletar: (id) => api.delete(`/exames/${id}`)
};

export default api;