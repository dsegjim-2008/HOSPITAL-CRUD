import axios from 'axios';

const API_URL = "http://localhost:8081/api";

export const hospitalService = {
    // MÉDICOS & LOGIN
    obtenerMedicos: () => axios.get(`${API_URL}/medicos`),
    // Simulamos login buscando en la lista (en un entorno real sería un POST a /login)
    login: async (usuario, password) => {
        const resp = await axios.get(`${API_URL}/medicos`);
        const medico = resp.data.find(m => m.usuario === usuario && password === "123");
        return medico;
    },

    // PACIENTES (Filtrados por médico si es necesario)
    obtenerPacientes: () => axios.get(`${API_URL}/pacientes`),
    crearPaciente: (paciente) => axios.post(`${API_URL}/pacientes`, paciente),
    obtenerHuerfanos: () => axios.get(`${API_URL}/pacientes/huerfanos`),

    // CITAS (La nueva joya)
    obtenerCitasMedico: (medicoId) => axios.get(`${API_URL}/citas/medico/${medicoId}`),
    crearCita: (cita) => axios.post(`${API_URL}/citas`, cita)
};