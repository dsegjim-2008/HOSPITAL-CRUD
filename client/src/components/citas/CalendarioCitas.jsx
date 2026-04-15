import React, { useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { Plus, Calendar as CalIcon } from 'lucide-react';
import { useCitas } from '../../hooks/useCitas';
import CitaModal from './CitaModal';
import CitasDiaModal from './CitasDiaModal';

function CalendarioCitas({ medicoId }) {
    const { citas, eventosCalendario, cargarAgenda } = useCitas(medicoId);

    // Estados de control de modales
    const [modalCrearOpen, setModalCrearOpen] = useState(false);
    const [modalListaOpen, setModalListaOpen] = useState(false);
    const [modalEditarOpen, setModalEditarOpen] = useState(false);

    const [fechaSeleccionada, setFechaSeleccionada] = useState("");
    const [citasDelDia, setCitasDelDia] = useState([]);
    const [citaAEditar, setCitaAEditar] = useState(null);

    const handleDateClick = (info) => {
        const fecha = info.dateStr;
        const filtradas = citas.filter(c => c.fechaHora.startsWith(fecha));

        setFechaSeleccionada(fecha);

        if (filtradas.length > 0) {
            setCitasDelDia(filtradas);
            setModalListaOpen(true);
        } else {
            setModalCrearOpen(true);
        }
    };

    return (
        <div className="bg-white rounded-[3rem] p-8 shadow-xl border border-slate-100">
            <div className="flex justify-between items-center mb-8">
                <h2 className="text-3xl font-black text-slate-800 flex items-center gap-3">
                    <CalIcon className="text-indigo-600" /> Agenda Mensual
                </h2>
                <button onClick={() => { setFechaSeleccionada(""); setModalCrearOpen(true); }} className="bg-indigo-600 text-white px-6 py-3 rounded-2xl font-bold flex items-center gap-2">
                    <Plus size={20} /> Nueva Cita
                </button>
            </div>

            <div className="custom-calendar">
                <FullCalendar
                    plugins={[dayGridPlugin, interactionPlugin]}
                    initialView="dayGridMonth"
                    locale="es"
                    events={eventosCalendario}
                    dateClick={handleDateClick}
                    headerToolbar={{
                        left: 'prev,next',
                        center: 'title',
                        right: ''
                    }}
                    height="auto"
                />
            </div>

            {/* MODAL 1: LISTA DEL DÍA */}
            {modalListaOpen && (
                <CitasDiaModal 
                    fecha={fechaSeleccionada} 
                    citas={citasDelDia} 
                    onClose={() => setModalListaOpen(false)}
                    onEditCita={(cita) => {
                        setCitaAEditar(cita);
                        setModalListaOpen(false);
                        setModalEditarOpen(true);
                    }}
                />
            )}

            {/* MODAL 2: CREAR NUEVA (Con fecha auto-asignada) */}
            {modalCrearOpen && (
                <CitaModal 
                    medicoId={medicoId} 
                    fechaPredefinida={fechaSeleccionada}
                    onClose={() => setModalCrearOpen(false)}
                    onUpdate={cargarAgenda}
                />
            )}

            {/* MODAL 3: EDITAR EXISTENTE */}
            {modalEditarOpen && (
                <CitaModal 
                    cita={citaAEditar}
                    medicoId={medicoId}
                    onClose={() => setModalEditarOpen(false)}
                    onUpdate={cargarAgenda}
                />
            )}
        </div>
    );
}

export default CalendarioCitas;
