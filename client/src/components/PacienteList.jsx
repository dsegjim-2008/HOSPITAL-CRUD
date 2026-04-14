import React, { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';
import { User, AlertTriangle, FileText, Edit, Activity, Search, Plus } from 'lucide-react';
import Swal from 'sweetalert2';
import EditPacienteModal from './EditPacienteModal';
import HistorialModal from './HistorialModal';

// Recibimos medicoId como prop desde App.jsx
function PacienteList({ medicoId }) {
    const [pacientes, setPacientes] = useState([]);
    const [busqueda, setBusqueda] = useState("");
    const [cargando, setCargando] = useState(true);
    const [pacienteSeleccionado, setPacienteSeleccionado] = useState(null);
    const [tipoModal, setTipoModal] = useState(null);

    useEffect(() => {
        cargarPacientes();
    }, [medicoId]); // Recargar si cambia el médico

    const cargarPacientes = async () => {
        try {
            const resp = await hospitalService.obtenerPacientes();
            let lista = resp.data;

            // --- FILTRO DE PRIVACIDAD ---
            // Si medicoId existe, solo nos quedamos con sus pacientes
            if (medicoId) {
                lista = lista.filter(p => p.medicoId === medicoId);
            }

            setPacientes(lista);
        } catch (error) {
            Swal.fire('Error', 'No se pudo conectar con el servidor', 'error');
        } finally {
            setCargando(false);
        }
    };

    const pacientesFiltrados = pacientes.filter(p => 
        p.nombre.toLowerCase().includes(busqueda.toLowerCase()) || 
        p.nss.includes(busqueda)
    );

    return (
        <div className="space-y-8">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div>
                    <h2 className="text-2xl font-black text-slate-800 flex items-center gap-2">
                        <Activity className="text-indigo-500" /> 
                        {medicoId ? 'Mis Pacientes Asignados' : 'Listado Global de Pacientes'}
                    </h2>
                    <p className="text-slate-400 text-sm font-medium">
                        Gestionando {pacientesFiltrados.length} expedientes
                    </p>
                </div>
                
                <div className="flex items-center gap-3">
                    <div className="relative w-full md:w-64">
                        <Search className="absolute left-4 top-3 text-slate-400" size={18} />
                        <input 
                            type="text" 
                            placeholder="Buscar..." 
                            className="w-full pl-11 pr-4 py-2.5 bg-white border border-slate-200 rounded-2xl focus:ring-2 focus:ring-indigo-500 outline-none transition-all shadow-sm"
                            onChange={(e) => setBusqueda(e.target.value)}
                        />
                    </div>
                    {/* Botón para añadir paciente (se asignará automáticamente si eres médico) */}
                    <button 
                        onClick={() => Swal.fire('Próximamente', 'Estamos habilitando el alta rápida de pacientes', 'info')}
                        className="bg-indigo-600 text-white p-3 rounded-2xl hover:bg-indigo-700 shadow-lg shadow-indigo-100 transition-all"
                    >
                        <Plus size={24} />
                    </button>
                </div>
            </div>

            {pacientesFiltrados.length > 0 ? (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {pacientesFiltrados.map(paciente => (
                        <div key={paciente.id} className="bg-white border border-slate-100 rounded-[2rem] p-6 shadow-sm hover:shadow-md transition-all border-b-4 border-b-indigo-500/10">
                            <div className="flex items-center gap-4 mb-5">
                                <div className="p-3 bg-indigo-50 text-indigo-600 rounded-2xl">
                                    <User size={24} />
                                </div>
                                <div>
                                    <h3 className="font-bold text-slate-800">{paciente.nombre} {paciente.apellido}</h3>
                                    <p className="text-xs font-mono text-slate-400 uppercase tracking-tighter">NSS: {paciente.nss}</p>
                                </div>
                            </div>

                            {!medicoId && (
                                <div className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-4 px-2">
                                    Médico: <span className="text-indigo-600">{paciente.nombreMedico}</span>
                                </div>
                            )}

                            <div className="grid grid-cols-2 gap-3">
                                <button 
                                    onClick={() => { setPacienteSeleccionado(paciente); setTipoModal('historial'); }}
                                    className="flex items-center justify-center gap-2 bg-slate-900 text-white py-2.5 rounded-xl hover:bg-slate-800 transition-all font-bold text-xs"
                                >
                                    <FileText size={14} /> Historial
                                </button>
                                <button 
                                    onClick={() => { setPacienteSeleccionado(paciente); setTipoModal('edit'); }}
                                    className="flex items-center justify-center gap-2 bg-white border border-slate-200 text-slate-600 py-2.5 rounded-xl hover:border-indigo-200 hover:text-indigo-600 transition-all font-bold text-xs"
                                >
                                    <Edit size={14} /> Editar
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="text-center py-20 bg-slate-50 rounded-[3rem] border-2 border-dashed border-slate-200">
                    <p className="text-slate-400 italic">No tienes pacientes asignados en este momento.</p>
                </div>
            )}

            {tipoModal === 'edit' && (
                <EditPacienteModal 
                    paciente={pacienteSeleccionado} 
                    onClose={() => setTipoModal(null)} 
                    onUpdate={cargarPacientes} 
                />
            )}

            {tipoModal === 'historial' && (
                <HistorialModal 
                    paciente={pacienteSeleccionado} 
                    onClose={() => setTipoModal(null)} 
                />
            )}
        </div>
    );
}

export default PacienteList;