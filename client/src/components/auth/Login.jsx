import React, { useState } from 'react';
import { HeartPulse, Lock, User } from 'lucide-react';
import { hospitalService } from '../../services/hospitalService';
import Swal from 'sweetalert2';

function Login({ onLogin }) {
    const [form, setForm] = useState({ usuario: '', password: '' });

    const handleSubmit = async (e) => {
        e.preventDefault();
        const medico = await hospitalService.login(form.usuario, form.password);
        
        if (medico) {
            Swal.fire({ title: `Bienvenido, Dr. ${medico.primerApellido}`, icon: 'success', timer: 1500, showConfirmButton: false });
            onLogin({ rol: 'medico', data: medico });
        } else if (form.usuario === 'admin' && form.password === 'admin') {
            onLogin({ rol: 'admin', data: { nombreCompleto: 'Administrador' } });
        } else {
            Swal.fire('Error', 'Credenciales incorrectas', 'error');
        }
    };

    return (
        <div className="min-h-screen bg-indigo-950 flex items-center justify-center p-6">
            <div className="bg-white w-full max-w-md rounded-[3rem] shadow-2xl overflow-hidden p-10">
                <div className="text-center mb-10">
                    <div className="bg-indigo-100 w-20 h-20 rounded-3xl flex items-center justify-center mx-auto mb-4 text-indigo-600">
                        <HeartPulse size={48} />
                    </div>
                    <h1 className="text-3xl font-black text-slate-800">HospitalOS</h1>
                    <p className="text-slate-400 font-medium">Acceso al Sistema Clínico</p>
                </div>

                <form onSubmit={handleSubmit} className="space-y-6">
                    <div className="relative">
                        <User className="absolute left-4 top-4 text-slate-300" size={20} />
                        <input 
                            type="text" placeholder="Usuario (ej: davseg1990)"
                            className="w-full pl-12 pr-4 py-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-indigo-500 transition-all"
                            onChange={e => setForm({...form, usuario: e.target.value})}
                        />
                    </div>
                    <div className="relative">
                        <Lock className="absolute left-4 top-4 text-slate-300" size={20} />
                        <input 
                            type="password" placeholder="Contraseña"
                            className="w-full pl-12 pr-4 py-4 bg-slate-50 border border-slate-100 rounded-2xl outline-none focus:ring-2 focus:ring-indigo-500 transition-all"
                            onChange={e => setForm({...form, password: e.target.value})}
                        />
                    </div>
                    <button className="w-full bg-indigo-600 text-white py-4 rounded-2xl font-bold text-lg shadow-lg shadow-indigo-200 hover:bg-indigo-700 transition-all active:scale-95">
                        Entrar al Panel
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Login;
