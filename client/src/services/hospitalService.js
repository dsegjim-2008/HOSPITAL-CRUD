import axios from 'axios';

const API_URL = "http://localhost:8081/api";

export const hospitalService = {
    // MÉDICOS
    obtenerMedicos: () => axios.get(`${API_URL}/medicos`),
    eliminarMedico: (id) => axios.delete(`${API_URL}/medicos/${id}`),
    
    // LOGIN
    login: async (usuario, password) => {
        const resp = await axios.get(`${API_URL}/medicos`);
        return resp.data.find(m => m.usuario === usuario && password === "123");
    },

    // PACIENTES
    obtenerPacientes: () => axios.get(`${API_URL}/pacientes`),
    obtenerHuerfanos: () => axios.get(`${API_URL}/pacientes/huerfanos`),
    
    // ESTA ES LA QUE TE FALTA SEGURO:
    actualizarPaciente: (id, datos) => axios.put(`${API_URL}/pacientes/${id}`, datos),

    // CITAS
    obtenerCitasMedico: (medicoId) => axios.get(`${API_URL}/citas/medico/${medicoId}`)
};