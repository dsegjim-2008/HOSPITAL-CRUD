import { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';

/**
 * Hook para gestionar el listado de pacientes sin médico asignado (huérfanos).
 *
 * @returns {{ huerfanos: Array, cargando: boolean, cargarHuerfanos: Function }}
 */
export function useHuerfanos() {
    const [huerfanos, setHuerfanos] = useState([]);
    const [cargando, setCargando] = useState(true);

    const cargarHuerfanos = async () => {
        setCargando(true);
        try {
            const resp = await hospitalService.obtenerHuerfanos();
            setHuerfanos(resp.data);
        } catch (err) {
            console.error('Error al cargar pacientes huérfanos', err);
        } finally {
            setCargando(false);
        }
    };

    useEffect(() => { cargarHuerfanos(); }, []);

    return { huerfanos, cargando, cargarHuerfanos };
}
