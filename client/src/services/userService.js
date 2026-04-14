import axios from 'axios';

const API_URL = 'http://localhost:8081/api/usuarios';

// Exportamos pequeñas funciones limpias
export const getUsuarios = () => axios.get(API_URL);
export const crearUsuario = (user) => axios.post(API_URL, user);
export const actualizarUsuario = (id, user) => axios.put(`${API_URL}/${id}`, user);
export const borrarUsuario = (id) => axios.delete(`${API_URL}/${id}`);
export const loginUsuario = (credenciales) => axios.post(`${API_URL}/login`, credenciales);