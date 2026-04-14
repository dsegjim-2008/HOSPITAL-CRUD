// client/src/components/CitasDiaModal.jsx
import React from 'react';
import { X, Edit3, Clock, MapPin, User } from 'lucide-react';

function CitasDiaModal({ fecha, citas, onClose, onEditCita }) {
    const fechaLegible = new Date(fecha).toLocaleDateString('es-ES', { 
        weekday: 'long', day: 'numeric', month: 'long' 
    });

    return (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center z-[110] p-4">
            <div className="bg-white rounded-[2.5rem] shadow-2xl w-full max-w-md overflow-hidden animate-in fade-in zoom-in duration-200">
                <div className="bg-slate-800 p-6 text-white flex justify-between items-center">
                    <div>
                        <h2 className="text-xl font-bold">Citas del día</h2>
                        <p className="text-slate-400 text-xs uppercase tracking-widest">{fechaLegible}</p>
                    </div>
                    <button onClick={onClose} className="hover:bg-slate-700 p-2 rounded-full transition"><X /></button>
                </div>

                <div className="p-6 space-y-4 max-h-[60vh] overflow-y-auto bg-slate-50">
                    {citas.map(cita => (
                        <div key={cita.id} className="bg-white p-4 rounded-2xl border border-slate-100 shadow-sm flex justify-between items-center">
                            <div>
                                <div className="flex items-center gap-2 text-indigo-600 font-bold text-sm mb-1">
                                    <Clock size={14} /> {cita.fechaHora.split('T')[1].substring(0,5)}
                                </div>
                                <p className="font-bold text-slate-800 flex items-center gap-2">
                                    <User size={14} className="text-slate-400"/> {cita.nombrePaciente}
                                </p>
                                <p className="text-xs text-slate-500 mt-1 flex items-center gap-1">
                                    <MapPin size={12}/> {cita.sala}
                                </p>
                            </div>
                            <button 
                                onClick={() => onEditCita(cita)}
                                className="p-3 bg-indigo-50 text-indigo-600 rounded-xl hover:bg-indigo-600 hover:text-white transition-colors"
                            >
                                <Edit3 size={18} />
                            </button>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default CitasDiaModal;