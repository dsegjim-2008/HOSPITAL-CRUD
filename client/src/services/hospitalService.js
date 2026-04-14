import axios from 'axios';

const API_URL = "http://localhost:8081/api";

export const hospitalService = {
    // MÉDICOS
    obtenerMedicos: () => axios.get(`${API_URL}/medicos`),
    eliminarMedico: (id) => axios.delete(`${API_URL}/medicos/${id}`),

    // LOGIN (Restaurado tal cual lo tenías en tu imagen)
    login: async (usuario, password) => {
        const resp = await axios.get(`${API_URL}/medicos`);
        return resp.data.find(m => m.usuario === usuario && password === "123");
    },

    // PACIENTES
    obtenerPacientes: () => axios.get(`${API_URL}/pacientes`),
    obtenerHuerfanos: () => axios.get(`${API_URL}/pacientes/huerfanos`),
    actualizarPaciente: (id, datos) => axios.put(`${API_URL}/pacientes/${id}`, datos),

    // CITAS (Añadidas las de gestión que necesitabas)
    obtenerCitasMedico: (medicoId) => axios.get(`${API_URL}/citas/medico/${medicoId}`),
    crearCita: (datos) => axios.post(`${API_URL}/citas`, datos),
    actualizarCita: (id, datos) => axios.put(`${API_URL}/citas/${id}`, datos),
    eliminarCita: (id) => axios.delete(`${API_URL}/citas/${id}`)
};