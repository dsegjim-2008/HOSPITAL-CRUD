import React, { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';
import { X, Save } from 'lucide-react';
import Swal from 'sweetalert2';

function EditPacienteModal({ paciente, onClose, onUpdate }) {
    const [formData, setFormData] = useState({ ...paciente });
    const [medicos, setMedicos] = useState([]);

    useEffect(() => {
        hospitalService.obtenerMedicos().then(resp => setMedicos(resp.data));
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await hospitalService.actualizarPaciente(paciente.id, formData);
            Swal.fire('¡Actualizado!', 'Los datos del paciente se han guardado.', 'success');
            onUpdate(); // Refrescar lista
            onClose();  // Cerrar modal
        } catch (error) {
            Swal.fire('Error', 'No se pudo actualizar el paciente', 'error');
        }
    };

    return (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center z-50 p-4">
            <div className="bg-white rounded-3xl shadow-2xl w-full max-w-md overflow-hidden animate-in fade-in zoom-in duration-200">
                <div className="bg-indigo-600 p-6 text-white flex justify-between items-center">
                    <h2 className="text-xl font-bold">Editar Paciente</h2>
                    <button onClick={onClose} className="hover:bg-indigo-500 p-2 rounded-full transition"><X /></button>
                </div>
                
                <form onSubmit={handleSubmit} className="p-8 space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-slate-700 mb-1">Nombre</label>
                        <input type="text" value={formData.nombre} onChange={e => setFormData({...formData, nombre: e.target.value})} className="w-full border border-slate-200 rounded-xl p-3 focus:ring-2 focus:ring-indigo-500 outline-none" required />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-slate-700 mb-1">Apellido</label>
                        <input type="text" value={formData.apellido} onChange={e => setFormData({...formData, apellido: e.target.value})} className="w-full border border-slate-200 rounded-xl p-3 focus:ring-2 focus:ring-indigo-500 outline-none" required />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-slate-700 mb-1">Asignar Médico</label>
                        <select value={formData.medicoId || ""} onChange={e => setFormData({...formData, medicoId: e.target.value || null})} className="w-full border border-slate-200 rounded-xl p-3 focus:ring-2 focus:ring-indigo-500 outline-none bg-white">
                            <option value="">-- Sin Médico (Huérfano) --</option>
                            {medicos.map(m => <option key={m.id} value={m.id}>{m.nombreCompleto} ({m.especialidad})</option>)}
                        </select>
                    </div>
                    <button type="submit" className="w-full bg-indigo-600 text-white font-bold p-4 rounded-xl hover:bg-indigo-700 transition flex justify-center gap-2 items-center shadow-lg shadow-indigo-200">
                        <Save size={20} /> Guardar Cambios
                    </button>
                </form>
            </div>
        </div>
    );
}

export default EditPacienteModal;