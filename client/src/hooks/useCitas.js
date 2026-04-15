import { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';

/**
 * Hook para gestionar la agenda de citas de un médico.
 * Devuelve las citas crudas y los eventos ya formateados para FullCalendar.
 *
 * @param {number} medicoId - ID del médico cuya agenda se carga.
 * @returns {{ citas: Array, eventosCalendario: Array, cargarAgenda: Function }}
 */
export function useCitas(medicoId) {
    const [citas, setCitas] = useState([]);
    const [eventosCalendario, setEventosCalendario] = useState([]);

    const cargarAgenda = async () => {
        const resp = await hospitalService.obtenerCitasMedico(medicoId);
        setCitas(resp.data);

        // Agrupa citas por día y genera los eventos para FullCalendar
        const events = Object.entries(
            resp.data.reduce((acc, c) => {
                const fecha = c.fechaHora.split('T')[0];
                acc[fecha] = (acc[fecha] || 0) + 1;
                return acc;
            }, {})
        ).map(([fecha, count]) => ({
            title: `${count} Cita${count > 1 ? 's' : ''}`,
            start: fecha,
            extendedProps: { fecha }
        }));

        setEventosCalendario(events);
    };

    useEffect(() => { cargarAgenda(); }, [medicoId]);

    return { citas, eventosCalendario, cargarAgenda };
}
