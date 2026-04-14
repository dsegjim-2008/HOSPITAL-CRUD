import React, { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';
import { Calendar as CalendarIcon, Clock, MapPin, User, Plus, RefreshCw } from 'lucide-react';
import Swal from 'sweetalert2';

function CalendarioCitas({ medicoId }) {
    const [citas, setCitas] = useState([]);
    const [cargando, setCargando] = useState(true);

    useEffect(() => {
        cargarCitas();
    }, [medicoId]);

    const cargarCitas = async () => {
        try {
            const resp = await hospitalService.obtenerCitasMedico(medicoId);
            setCitas(resp.data);
        } catch (error) {
            console.error("Error al cargar la agenda", error);
        } finally {
            setCargando(false);
        }
    };

    const formatearHora = (fechaIso) => {
        return new Date(fechaIso).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    };

    const formatearFecha = (fechaIso) => {
        return new Date(fechaIso).toLocaleDateString('es-ES', { 
            weekday: 'long', 
            day: 'numeric', 
            month: 'long' 
        });
    };

    if (cargando) return <div className="text-center py-20 animate-pulse text-indigo-600 font-bold">Sincronizando agenda médica...</div>;

    return (
        <div className="max-w-4xl mx-auto space-y-8">
            {/* Cabecera de la Agenda */}
            <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 bg-white p-8 rounded-[2.5rem] shadow-sm border border-slate-100">
                <div>
                    <h2 className="text-3xl font-black text-slate-800 flex items-center gap-3">
                        <CalendarIcon className="text-indigo-600" size={32} /> Mi Agenda
                    </h2>
                    <p className="text-slate-400 font-medium mt-1 uppercase tracking-widest text-xs">
                        {citas.length > 0 ? formatearFecha(citas[0].fechaHora) : 'Sin citas programadas'}
                    </p>
                </div>
                <button 
                    onClick={() => Swal.fire('Función en desarrollo', 'Pronto podrás programar citas desde aquí', 'info')}
                    className="bg-indigo-600 text-white px-6 py-4 rounded-2xl flex items-center gap-3 font-bold hover:bg-indigo-700 transition-all shadow-lg shadow-indigo-100 active:scale-95"
                >
                    <Plus size={20} /> Nueva Cita
                </button>
            </div>

            {/* Listado de Citas (Agenda) */}
            <div className="space-y-4">
                {citas.length > 0 ? (
                    citas.map((cita) => (
                        <div key={cita.id} className="group bg-white border border-slate-100 rounded-3xl p-6 flex flex-col md:flex-row items-center gap-6 hover:shadow-xl hover:border-indigo-200 transition-all duration-300">
                            {/* Bloque Hora */}
                            <div className="flex flex-col items-center justify-center bg-slate-50 px-6 py-4 rounded-2xl border border-slate-100 min-w-[120px]">
                                <span className="text-2xl font-black text-slate-800">{formatearHora(cita.fechaHora)}</span>
                                <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Hora Cita</span>
                            </div>

                            {/* Info Paciente */}
                            <div className="flex-1">
                                <div className="flex items-center gap-2 text-indigo-600 mb-1">
                                    <User size={16} />
                                    <span className="text-xs font-bold uppercase tracking-wider">Paciente</span>
                                </div>
                                <h3 className="text-xl font-bold text-slate-900">{cita.nombrePaciente}</h3>
                                <p className="text-slate-500 text-sm mt-1 flex items-center gap-1 italic">
                                    "{cita.motivo}"
                                </p>
                            </div>

                            {/* Detalles Sala */}
                            <div className="flex items-center gap-8 pr-4">
                                <div className="flex items-center gap-3 bg-indigo-50 text-indigo-700 px-4 py-2 rounded-xl border border-indigo-100">
                                    <MapPin size={18} />
                                    <span className="font-bold text-sm">{cita.sala}</span>
                                </div>
                                <div className="w-3 h-3 bg-emerald-400 rounded-full animate-pulse shadow-[0_0_10px_rgba(52,211,153,0.5)]"></div>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="text-center py-24 bg-slate-50 rounded-[3rem] border-2 border-dashed border-slate-200">
                        <RefreshCw className="mx-auto text-slate-300 mb-4 animate-spin-slow" size={48} />
                        <p className="text-slate-400 font-medium italic">No tienes citas para hoy. ¡Día tranquilo!</p>
                    </div>
                )}
            </div>
        </div>
    );
}

// ESTA ES LA LÍNEA QUE TE FALTABA Y CAUSABA EL ERROR:
export default CalendarioCitas;