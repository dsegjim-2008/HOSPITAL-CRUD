import React, { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';
import { User, AlertTriangle, FileText, Edit, Activity, Search } from 'lucide-react';
import Swal from 'sweetalert2';

// Importamos los componentes modales
import EditPacienteModal from './EditPacienteModal';
import HistorialModal from './HistorialModal';

function PacienteList() {
    const [pacientes, setPacientes] = useState([]);
    const [busqueda, setBusqueda] = useState("");
    const [cargando, setCargando] = useState(true);
    
    // Estados para el manejo de modales
    const [pacienteSeleccionado, setPacienteSeleccionado] = useState(null);
    const [tipoModal, setTipoModal] = useState(null); // 'edit' | 'historial' | null

    useEffect(() => {
        cargarPacientes();
    }, []);

    const cargarPacientes = async () => {
        try {
            const resp = await hospitalService.obtenerPacientes();
            setPacientes(resp.data);
        } catch (error) {
            Swal.fire('Error', 'No se pudo conectar con el servidor', 'error');
        } finally {
            setCargando(false);
        }
    };

    // Filtro de búsqueda por nombre o NSS
    const pacientesFiltrados = pacientes.filter(p => 
        p.nombre.toLowerCase().includes(busqueda.toLowerCase()) || 
        p.nss.includes(busqueda)
    );

    const abrirModal = (paciente, tipo) => {
        setPacienteSeleccionado(paciente);
        setTipoModal(tipo);
    };

    const cerrarModal = () => {
        setPacienteSeleccionado(null);
        setTipoModal(null);
    };

    if (cargando) return (
        <div className="flex flex-col items-center justify-center py-20 space-y-4">
            <div className="w-12 h-12 border-4 border-indigo-600 border-t-transparent rounded-full animate-spin"></div>
            <p className="text-indigo-600 font-bold animate-pulse">Sincronizando expedientes...</p>
        </div>
    );

    return (
        <div className="space-y-8">
            {/* Barra de herramientas superior */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <h2 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
                    <Activity className="text-indigo-500" /> 
                    Listado de Pacientes 
                    <span className="ml-2 text-sm bg-indigo-100 text-indigo-600 px-3 py-1 rounded-full">
                        {pacientes.length} totales
                    </span>
                </h2>
                
                <div className="relative w-full md:w-96">
                    <Search className="absolute left-4 top-3 text-slate-400" size={20} />
                    <input 
                        type="text" 
                        placeholder="Buscar por nombre o NSS..." 
                        value={busqueda}
                        onChange={(e) => setBusqueda(e.target.value)}
                        className="w-full pl-12 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-2xl focus:ring-2 focus:ring-indigo-500 outline-none transition-all shadow-inner"
                    />
                </div>
            </div>

            {/* Grid de Pacientes */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                {pacientesFiltrados.map(paciente => {
                    const esHuerfano = !paciente.medicoId;
                    
                    return (
                        <div 
                            key={paciente.id} 
                            className={`group relative bg-white border rounded-[2rem] p-7 transition-all duration-300 hover:shadow-xl hover:-translate-y-1
                                ${esHuerfano ? 'border-red-200 bg-gradient-to-br from-white to-red-50/30' : 'border-slate-100'}`}
                        >
                            {/* Indicador de Alerta para Huérfanos */}
                            {esHuerfano && (
                                <div className="absolute -top-3 -right-3 bg-red-500 text-white p-2 rounded-full shadow-lg animate-bounce">
                                    <AlertTriangle size={20} />
                                </div>
                            )}

                            <div className="flex items-start gap-4 mb-6">
                                <div className={`p-4 rounded-2xl shadow-sm ${esHuerfano ? 'bg-red-100 text-red-600' : 'bg-indigo-50 text-indigo-600'}`}>
                                    <User size={28} />
                                </div>
                                <div className="flex-1">
                                    <h3 className="text-xl font-bold text-slate-900 group-hover:text-indigo-600 transition-colors">
                                        {paciente.nombre} {paciente.apellido}
                                    </h3>
                                    <p className="text-sm font-mono text-slate-400 mt-1 uppercase tracking-tighter">
                                        NSS: {paciente.nss}
                                    </p>
                                </div>
                            </div>

                            <div className="bg-slate-50/80 rounded-2xl p-4 mb-6 border border-slate-100">
                                <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-2">Médico Responsable</p>
                                {esHuerfano ? (
                                    <div className="flex items-center gap-2 text-red-600 font-bold text-sm">
                                        <div className="w-2 h-2 bg-red-500 rounded-full animate-ping"></div>
                                        PENDIENTE DE ASIGNACIÓN
                                    </div>
                                ) : (
                                    <p className="font-semibold text-slate-700">{paciente.nombreMedico}</p>
                                )}
                            </div>

                            {/* Botonera de acciones */}
                            <div className="grid grid-cols-2 gap-3">
                                <button 
                                    onClick={() => abrirModal(paciente, 'historial')}
                                    className="flex items-center justify-center gap-2 bg-slate-900 text-white py-3 rounded-xl hover:bg-slate-800 transition-all font-bold text-sm"
                                >
                                    <FileText size={16} /> Historial
                                </button>
                                <button 
                                    onClick={() => abrirModal(paciente, 'edit')}
                                    className="flex items-center justify-center gap-2 bg-white border-2 border-slate-100 text-slate-600 py-3 rounded-xl hover:border-indigo-200 hover:text-indigo-600 transition-all font-bold text-sm"
                                >
                                    <Edit size={16} /> Editar
                                </button>
                            </div>
                        </div>
                    );
                })}
            </div>

            {/* Mensaje si no hay resultados en la búsqueda */}
            {pacientesFiltrados.length === 0 && (
                <div className="text-center py-20">
                    <p className="text-slate-400 text-lg italic font-serif">No se encontraron pacientes que coincidan con "{busqueda}"</p>
                </div>
            )}

            {/* --- RENDERIZADO DE MODALES EMERGENTES --- */}
            
            {tipoModal === 'edit' && (
                <EditPacienteModal 
                    paciente={pacienteSeleccionado} 
                    onClose={cerrarModal} 
                    onUpdate={cargarPacientes} 
                />
            )}

            {tipoModal === 'historial' && (
                <HistorialModal 
                    paciente={pacienteSeleccionado} 
                    onClose={cerrarModal} 
                />
            )}
        </div>
    );
}

export default PacienteList;