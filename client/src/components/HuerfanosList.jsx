import React, { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';
import { AlertTriangle, UserPlus, RefreshCcw } from 'lucide-react';
import EditPacienteModal from './EditPacienteModal'; // Reutilizamos el modal
import Swal from 'sweetalert2';

function HuerfanosList() {
    const [huerfanos, setHuerfanos] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [pacienteAEditar, setPacienteAEditar] = useState(null);

    useEffect(() => {
        cargarHuerfanos();
    }, []);

    const cargarHuerfanos = async () => {
        try {
            const resp = await hospitalService.obtenerHuerfanos();
            setHuerfanos(resp.data);
        } catch (error) {
            console.error("Error al cargar huérfanos", error);
        } finally {
            setCargando(false);
        }
    };

    if (cargando) return <div className="text-center py-20 text-indigo-600 animate-pulse font-bold">Buscando pacientes sin asignar...</div>;

    if (huerfanos.length === 0) return (
        <div className="text-center py-24 bg-emerald-50 rounded-[3rem] border-2 border-dashed border-emerald-200">
            <div className="bg-emerald-100 w-20 h-20 rounded-full flex items-center justify-center mx-auto mb-6">
                <RefreshCcw className="text-emerald-600" size={40} />
            </div>
            <h3 className="text-2xl font-bold text-emerald-800">¡Todo bajo control!</h3>
            <p className="text-emerald-600 mt-2">No hay pacientes pendientes de asignación en este momento.</p>
        </div>
    );

    return (
        <div className="space-y-6">
            {/* Banner de aviso */}
            <div className="bg-gradient-to-r from-amber-500 to-orange-600 p-6 rounded-3xl text-white shadow-lg shadow-amber-100 flex items-center gap-6">
                <div className="bg-white/20 p-3 rounded-2xl backdrop-blur-md">
                    <AlertTriangle size={32} />
                </div>
                <div>
                    <h2 className="text-xl font-bold">Atención Requerida</h2>
                    <p className="opacity-90 font-medium">Hay {huerfanos.length} pacientes que han perdido su médico y necesitan ser reasignados.</p>
                </div>
            </div>
            
            <div className="grid grid-cols-1 gap-4">
                {huerfanos.map(p => (
                    <div key={p.id} className="group bg-white border border-slate-100 p-5 rounded-2xl flex flex-col md:flex-row justify-between items-center shadow-sm hover:shadow-md hover:border-indigo-200 transition-all">
                        <div className="flex items-center gap-4 mb-4 md:mb-0">
                            <div className="bg-slate-100 p-3 rounded-xl text-slate-500 group-hover:bg-indigo-50 group-hover:text-indigo-600 transition-colors">
                                <UserPlus size={24} />
                            </div>
                            <div>
                                <p className="font-bold text-slate-800 text-lg">{p.nombre} {p.apellido}</p>
                                <p className="text-xs text-slate-400 font-mono tracking-widest uppercase">Seguridad Social: {p.nss}</p>
                            </div>
                        </div>

                        <button 
                            onClick={() => setPacienteAEditar(p)}
                            className="w-full md:w-auto flex items-center justify-center gap-2 bg-indigo-600 text-white px-8 py-3 rounded-xl hover:bg-indigo-700 font-bold transition-all shadow-lg shadow-indigo-100 active:scale-95"
                        >
                            <UserPlus size={18} /> Reasignar Médico
                        </button>
                    </div>
                ))}
            </div>

            {/* MODAL REUTILIZADO */}
            {pacienteAEditar && (
                <EditPacienteModal 
                    paciente={pacienteAEditar} 
                    onClose={() => setPacienteAEditar(null)} 
                    onUpdate={() => {
                        cargarHuerfanos();
                        Swal.fire({
                            title: '¡Reasignado!',
                            text: 'El paciente ya tiene un nuevo médico responsable.',
                            icon: 'success',
                            confirmButtonColor: '#4f46e5',
                            customClass: { popup: 'rounded-3xl' }
                        });
                    }} 
                />
            )}
        </div>
    );
}

export default HuerfanosList;