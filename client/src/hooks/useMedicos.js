import { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';

/**
 * Hook para gestionar el listado de médicos.
 * Encapsula el estado y la lógica de fetching, separándola del componente de presentación.
 *
 * @returns {{ medicos: Array, cargando: boolean, cargarMedicos: Function }}
 */
export function useMedicos() {
    const [medicos, setMedicos] = useState([]);
    const [cargando, setCargando] = useState(true);

    const cargarMedicos = async () => {
        setCargando(true);
        try {
            const resp = await hospitalService.obtenerMedicos();
            setMedicos(resp.data);
        } finally {
            setCargando(false);
        }
    };

    useEffect(() => { cargarMedicos(); }, []);

    return { medicos, cargando, cargarMedicos };
}
