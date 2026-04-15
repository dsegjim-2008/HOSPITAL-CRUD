import { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';

/**
 * Hook para gestionar el listado de pacientes, con filtrado opcional por médico.
 * Si se proporciona medicoId, solo devuelve los pacientes de ese médico.
 *
 * @param {number|null} medicoId - ID del médico para filtrar, o null para todos.
 * @returns {{ pacientes: Array, cargando: boolean, error: string|null, cargarPacientes: Function }}
 */
export function usePacientes(medicoId = null) {
    const [pacientes, setPacientes] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState(null);

    const cargarPacientes = async () => {
        setCargando(true);
        setError(null);
        try {
            const resp = await hospitalService.obtenerPacientes();
            let lista = resp.data;
            if (medicoId) lista = lista.filter(p => p.medicoId === medicoId);
            setPacientes(lista);
        } catch {
            setError('No se pudo conectar con el servidor');
        } finally {
            setCargando(false);
        }
    };

    useEffect(() => { cargarPacientes(); }, [medicoId]);

    return { pacientes, cargando, error, cargarPacientes };
}
