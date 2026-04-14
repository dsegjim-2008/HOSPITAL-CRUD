import React, { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';
import { Stethoscope, Trash2, UserCheck, IdCard } from 'lucide-react';
import Swal from 'sweetalert2';

function MedicoList() {
    const [medicos, setMedicos] = useState([]);

    useEffect(() => { cargarMedicos(); }, []);

    const cargarMedicos = async () => {
        const resp = await hospitalService.obtenerMedicos();
        setMedicos(resp.data);
    };

    const eliminarMedico = async (id) => {
        const result = await Swal.fire({
            title: '¿Estás seguro?',
            text: "¡El médico se borrará pero sus pacientes quedarán huérfanos para reasignar!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#4f46e5',
            cancelButtonColor: '#94a3b8',
            confirmButtonText: 'Sí, borrar médico',
            cancelButtonText: 'Cancelar'
        });

        if (result.isConfirmed) {
            try {
                await hospitalService.eliminarMedico(id);
                Swal.fire('¡Eliminado!', 'El médico ha sido borrado.', 'success');
                cargarMedicos();
            } catch (error) {
                Swal.fire('Error', 'No se pudo eliminar el médico.', 'error');
            }
        }
    };

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {medicos.map(medico => (
                <div key={medico.id} className="bg-white border border-slate-200 rounded-2xl p-6 flex items-center justify-between hover:border-indigo-300 transition-colors shadow-sm">
                    <div className="flex items-center gap-5">
                        <div className="bg-indigo-600 p-4 rounded-xl text-white">
                            <Stethoscope size={28} />
                        </div>
                        <div>
                            <h3 className="text-xl font-bold text-slate-900">{medico.nombreCompleto}</h3>
                            <div className="flex gap-4 mt-1">
                                <span className="text-indigo-600 font-semibold text-sm px-2 py-0.5 bg-indigo-50 rounded-md">
                                    {medico.especialidad}
                                </span>
                                <span className="text-slate-400 text-sm flex items-center gap-1">
                                    <IdCard size={14}/> {medico.numeroColegiado}
                                </span>
                            </div>
                        </div>
                    </div>
                    <button 
                        onClick={() => eliminarMedico(medico.id)}
                        className="p-3 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-full transition"
                        title="Eliminar médico"
                    >
                        <Trash2 size={22} />
                    </button>
                </div>
            ))}
        </div>
    );
}

export default MedicoList;