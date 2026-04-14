import React, { useState } from 'react';
import Swal from 'sweetalert2';
import { loginUsuario, crearUsuario } from '../services/userService';

const Auth = ({ onLoginSuccess }) => {
  const [esRegistro, setEsRegistro] = useState(false);
  const [formData, setFormData] = useState({
    primerNombre: '', primerApellido: '', email: '', password: '', rol: 'USER'
  });

  const manejarCambio = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Función para validar el formato del email
  const validarEmail = (email) => {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!validarEmail(formData.email)) {
      Swal.fire({ icon: 'warning', title: 'Email inválido', text: 'Por favor, introduce un correo electrónico válido (ej: nombre@empresa.com)' });
      return;
    }

    Swal.fire({ title: 'Procesando...', allowOutsideClick: false, didOpen: () => Swal.showLoading() });

    if (esRegistro) {
      // REGISTRO
      crearUsuario(formData)
        .then(res => {
          Swal.close();
          onLoginSuccess(res.data); // Iniciamos sesión automáticamente tras registrarse
        })
        .catch(err => Swal.fire({ icon: 'error', title: 'Error al registrar', text: err.response?.data?.error }));
    } else {
      // LOGIN
      loginUsuario({ email: formData.email, password: formData.password })
        .then(res => {
          Swal.close();
          onLoginSuccess(res.data);
        })
        .catch(() => Swal.fire({ icon: 'error', title: 'Error', text: 'Email o contraseña incorrectos' }));
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-xl shadow-lg w-full max-w-md">
        <h2 className="text-2xl font-bold text-center text-gray-800 mb-6">
          {esRegistro ? 'Crear una Cuenta' : 'Iniciar Sesión'}
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Campos extra que solo se muestran al Registrarse */}
          {esRegistro && (
            <div className="grid grid-cols-2 gap-4">
              <input type="text" name="primerNombre" placeholder="Nombre *" required onChange={manejarCambio} className="border rounded px-3 py-2 w-full focus:ring-2 focus:ring-blue-500 outline-none" />
              <input type="text" name="primerApellido" placeholder="Apellido *" required onChange={manejarCambio} className="border rounded px-3 py-2 w-full focus:ring-2 focus:ring-blue-500 outline-none" />
            </div>
          )}

          <input type="email" name="email" placeholder="Email *" required onChange={manejarCambio} className="border rounded px-3 py-2 w-full focus:ring-2 focus:ring-blue-500 outline-none" />
          <input type="password" name="password" placeholder="Contraseña *" required onChange={manejarCambio} className="border rounded px-3 py-2 w-full focus:ring-2 focus:ring-blue-500 outline-none" />

          {/* Selector de Rol solo al Registrarse */}
          {esRegistro && (
            <div>
              <label className="block text-sm text-gray-600 mb-1">Selecciona tu Rol</label>
              <select name="rol" onChange={manejarCambio} className="border rounded px-3 py-2 w-full bg-white focus:ring-2 focus:ring-blue-500 outline-none">
                <option value="USER">Usuario Estándar (Ver tabla)</option>
                <option value="ADMIN">Administrador (Control Total)</option>
              </select>
            </div>
          )}

          <button type="submit" className="w-full bg-blue-600 text-white font-bold py-2 rounded hover:bg-blue-700 transition-colors">
            {esRegistro ? 'Registrarse' : 'Entrar'}
          </button>
        </form>

        <div className="mt-4 text-center">
          <button onClick={() => setEsRegistro(!esRegistro)} className="text-sm text-blue-600 hover:underline">
            {esRegistro ? '¿Ya tienes cuenta? Inicia sesión' : '¿No tienes cuenta? Regístrate aquí'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default Auth;