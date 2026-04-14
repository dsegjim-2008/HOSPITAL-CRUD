import React, { useState, useEffect } from 'react';
import { hospitalService } from '../services/hospitalService';
import { X, Save, Trash2 } from 'lucide-react';
import Swal from 'sweetalert2';

function CitaModal({ cita, medicoId, fechaPredefinida, onClose, onUpdate }) {
    const [form, setForm] = useState({
        pacienteId: cita?.pacienteId || '',
        motivo: cita?.motivo || '',
        sala: cita?.sala || '',
        fecha: cita ? cita.fechaHora.split('T')[0] : (fechaPredefinida || ''),
        hora: cita ? cita.fechaHora.split('T')[1].substring(0, 5) : '09:00'
    });

    const [pacientes, setPacientes] = useState([]);

    useEffect(() => {
        hospitalService.obtenerPacientes().then(resp => {
            setPacientes(resp.data.filter(p => p.medicoId === medicoId));
        });
    }, [medicoId]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Formato ISO local para el backend
        const payload = {
            ...form,
            fechaHora: `${form.fecha}T${form.hora}:00`,
            medicoId: medicoId,
            pacienteId: parseInt(form.pacienteId)
        };

        try {
            if (cita) {
                await hospitalService.actualizarCita(cita.id, payload);
            } else {
                await hospitalService.crearCita(payload);
            }
            Swal.fire({ icon: 'success', title: 'Guardado', timer: 1500, showConfirmButton: false });
            onUpdate();
            onClose();
        } catch (error) {
            Swal.fire('Error', 'Revisa los datos o la conexión con el servidor', 'error');
        }
    };

    return (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center z-[120] p-4">
            <div className="bg-white rounded-[2.5rem] shadow-2xl w-full max-w-lg overflow-hidden animate-in zoom-in duration-200">
                <div className="bg-indigo-600 p-6 text-white flex justify-between items-center">
                    <h2 className="text-xl font-bold">{cita ? 'Editar Cita' : 'Nueva Cita'}</h2>
                    <button onClick={onClose} className="hover:bg-white/20 p-2 rounded-full transition"><X /></button>
                </div>
                <form onSubmit={handleSubmit} className="p-8 space-y-4">
                    <label className="block">
                        <span className="text-xs font-bold text-slate-400 uppercase">Paciente</span>
                        <select required value={form.pacienteId} onChange={e => setForm({...form, pacienteId: e.target.value})} className="w-full mt-1 bg-slate-50 border-none rounded-2xl p-4 focus:ring-2 focus:ring-indigo-500">
                            <option value="">Seleccionar...</option>
                            {pacientes.map(p => <option key={p.id} value={p.id}>{p.nombre} {p.apellido}</option>)}
                        </select>
                    </label>
                    <div className="grid grid-cols-2 gap-4">
                        <label className="block">
                            <span className="text-xs font-bold text-slate-400 uppercase">Fecha</span>
                            <input type="date" value={form.fecha} onChange={e => setForm({...form, fecha: e.target.value})} className="w-full mt-1 bg-slate-50 border-none rounded-2xl p-4" />
                        </label>
                        <label className="block">
                            <span className="text-xs font-bold text-slate-400 uppercase">Hora</span>
                            <input type="time" value={form.hora} onChange={e => setForm({...form, hora: e.target.value})} className="w-full mt-1 bg-slate-50 border-none rounded-2xl p-4" />
                        </label>
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                        <input placeholder="Sala" value={form.sala} onChange={e => setForm({...form, sala: e.target.value})} className="bg-slate-50 border-none rounded-2xl p-4" />
                        <input placeholder="Motivo" value={form.motivo} onChange={e => setForm({...form, motivo: e.target.value})} className="bg-slate-50 border-none rounded-2xl p-4" />
                    </div>
                    <button className="w-full bg-indigo-600 text-white font-bold p-4 rounded-2xl mt-4 shadow-lg hover:bg-indigo-700 transition">
                        {cita ? 'Actualizar Cita' : 'Crear Cita'}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default CitaModal;