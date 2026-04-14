import React from 'react';
import { X, Calendar, ClipboardList } from 'lucide-react';

function HistorialModal({ paciente, onClose }) {
    return (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center z-50 p-4">
            <div className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl max-h-[85vh] overflow-hidden flex flex-col">
                <div className="bg-slate-800 p-6 text-white flex justify-between items-center">
                    <div>
                        <h2 className="text-xl font-bold">Historial Clínico</h2>
                        <p className="text-slate-400 text-sm">{paciente.nombre} {paciente.apellido}</p>
                    </div>
                    <button onClick={onClose} className="hover:bg-slate-700 p-2 rounded-full transition"><X /></button>
                </div>

                <div className="flex-1 overflow-y-auto p-8 space-y-6 bg-slate-50">
                    {paciente.episodios?.length > 0 ? (
                        paciente.episodios.map((ep, index) => (
                            <div key={ep.id} className="relative pl-8 border-l-2 border-indigo-200 pb-2">
                                <div className="absolute -left-[9px] top-0 w-4 h-4 rounded-full bg-indigo-500 border-4 border-white shadow-sm"></div>
                                <div className="bg-white rounded-2xl p-5 shadow-sm border border-slate-100">
                                    <div className="flex items-center gap-2 text-indigo-600 font-bold mb-2">
                                        <Calendar size={16} /> {new Date(ep.fecha).toLocaleDateString()}
                                    </div>
                                    <h4 className="text-lg font-bold text-slate-800 mb-1">{ep.diagnostico}</h4>
                                    <p className="text-slate-600 bg-slate-50 p-3 rounded-lg border border-slate-100 italic">
                                        <ClipboardList size={14} className="inline mr-2" />
                                        {ep.tratamiento}
                                    </p>
                                </div>
                            </div>
                        ))
                    ) : (
                        <div className="text-center py-10 text-slate-400">Este paciente no tiene episodios registrados.</div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default HistorialModal;