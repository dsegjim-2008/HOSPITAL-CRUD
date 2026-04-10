import React, { useEffect, useState } from 'react';
import axios from 'axios';

const UserList = () => {
  // --- ESTADOS DE LA TABLA ---
  const [usuarios, setUsuarios] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);

  // --- ESTADOS DEL MODAL Y FORMULARIO ---
  const [mostrarModal, setMostrarModal] = useState(false);
  const [usuarioActual, setUsuarioActual] = useState(null); // null = Crear, objeto = Editar
  const [formData, setFormData] = useState({
    primerNombre: '', segundoNombre: '', primerApellido: '', segundoApellido: '', email: '', password: '', rol: 'USER'
  });

  // --- CARGAR USUARIOS (GET) ---
  useEffect(() => {
    axios.get('http://localhost:8081/api/usuarios')
      .then(respuesta => {
        setUsuarios(respuesta.data);
        setCargando(false);
      })
      .catch(err => {
        console.error("Error al obtener usuarios:", err);
        setError("No se pudieron cargar los usuarios.");
        setCargando(false);
      });
  }, []);

  // --- BORRAR USUARIO (DELETE) ---
  const eliminarUsuario = (id, nombre) => {
    if (window.confirm(`¿Estás seguro de que quieres borrar a ${nombre}?`)) {
      axios.delete(`http://localhost:8081/api/usuarios/${id}`)
        .then(() => {
          setUsuarios(usuarios.filter(user => user.id !== id));
        })
        .catch(err => {
          alert("Hubo un error al intentar eliminar el usuario.");
        });
    }
  };

  // --- FUNCIONES DEL MODAL ---
  const abrirModalCrear = () => {
    setFormData({ primerNombre: '', segundoNombre: '', primerApellido: '', segundoApellido: '', email: '', password: '', rol: 'USER' });
    setUsuarioActual(null);
    setMostrarModal(true);
  };

  const abrirModalEditar = (user) => {
    setFormData({
      primerNombre: user.primerNombre || '',
      segundoNombre: user.segundoNombre || '',
      primerApellido: user.primerApellido || '',
      segundoApellido: user.segundoApellido || '',
      email: user.email || '',
      password: '', // Lo dejamos en blanco por seguridad. El backend lo ignora si no se envía.
      rol: user.rol || 'USER'
    });
    setUsuarioActual(user);
    setMostrarModal(true);
  };

  const cerrarModal = () => {
    setMostrarModal(false);
  };

  // --- MANEJO DEL FORMULARIO ---
  const manejarCambioInput = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const guardarUsuario = (e) => {
    e.preventDefault(); // Evita que la página se recargue al enviar el formulario

    if (usuarioActual) {
      // EDITAR (PUT)
      axios.put(`http://localhost:8081/api/usuarios/${usuarioActual.id}`, formData)
        .then(respuesta => {
          // Actualizamos la lista sustituyendo el usuario viejo por el nuevo
          setUsuarios(usuarios.map(u => u.id === usuarioActual.id ? respuesta.data : u));
          cerrarModal();
        })
        .catch(err => {
          alert("Error al editar: " + (err.response?.data?.error || "Error desconocido"));
        });
    } else {
      // CREAR (POST)
      axios.post('http://localhost:8081/api/usuarios', formData)
        .then(respuesta => {
          // Añadimos el nuevo usuario a la lista
          setUsuarios([...usuarios, respuesta.data]);
          cerrarModal();
        })
        .catch(err => {
          alert("Error al crear: " + (err.response?.data?.error || "Error desconocido"));
        });
    }
  };

  // --- RENDERIZADO (UI) ---
  if (cargando) return <div className="text-center p-10 text-xl">Cargando usuarios...</div>;
  if (error) return <div className="text-center p-10 text-xl text-red-500">{error}</div>;

  return (
    <div className="container mx-auto p-6 relative">
      {/* Cabecera y botón de crear */}
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800">Lista de Usuarios</h2>
        <button 
          onClick={abrirModalCrear}
          className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded shadow"
        >
          + Nuevo Usuario
        </button>
      </div>

      {/* Tabla de usuarios */}
      <div className="bg-white shadow-md rounded-lg overflow-hidden">
        <table className="min-w-full leading-normal">
          <thead>
            <tr>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Nombre Completo</th>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Email</th>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Rol</th>
              <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-50 text-center text-xs font-semibold text-gray-600 uppercase tracking-wider">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {usuarios.length === 0 ? (
              <tr><td colSpan="4" className="text-center py-5">No hay usuarios registrados.</td></tr>
            ) : (
              usuarios.map((user) => (
                <tr key={user.id} className="hover:bg-gray-50">
                  <td className="px-5 py-5 border-b border-gray-200 text-sm">{user.primerNombre} {user.primerApellido}</td>
                  <td className="px-5 py-5 border-b border-gray-200 text-sm">{user.email}</td>
                  <td className="px-5 py-5 border-b border-gray-200 text-sm">
                    <span className={`px-3 py-1 rounded-full text-xs font-bold ${user.rol === 'ADMIN' ? 'bg-red-200 text-red-900' : 'bg-green-200 text-green-900'}`}>
                      {user.rol}
                    </span>
                  </td>
                  <td className="px-5 py-5 border-b border-gray-200 text-sm text-center space-x-4">
                    {/* Botón de Editar conectado */}
                    <button onClick={() => abrirModalEditar(user)} className="text-indigo-600 hover:text-indigo-900 font-semibold">Editar</button>
                    {/* Botón de Borrar (Ya lo tenías) */}
                    <button onClick={() => eliminarUsuario(user.id, user.primerNombre)} className="text-red-600 hover:text-red-900 font-semibold">Borrar</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* --- MODAL DE CREACIÓN / EDICIÓN --- */}
      {mostrarModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
          <div className="bg-white rounded-lg shadow-xl w-full max-w-lg mx-4">
            
            <div className="flex justify-between items-center p-6 border-b">
              <h3 className="text-xl font-semibold text-gray-900">
                {usuarioActual ? 'Editar Usuario' : 'Crear Nuevo Usuario'}
              </h3>
              <button onClick={cerrarModal} className="text-gray-400 hover:text-gray-600 text-2xl font-bold">&times;</button>
            </div>

            <form onSubmit={guardarUsuario} className="p-6">
              <div className="grid grid-cols-2 gap-4 mb-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Primer Nombre *</label>
                  <input type="text" name="primerNombre" value={formData.primerNombre} onChange={manejarCambioInput} required className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Segundo Nombre</label>
                  <input type="text" name="segundoNombre" value={formData.segundoNombre} onChange={manejarCambioInput} className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4 mb-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Primer Apellido *</label>
                  <input type="text" name="primerApellido" value={formData.primerApellido} onChange={manejarCambioInput} required className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Segundo Apellido</label>
                  <input type="text" name="segundoApellido" value={formData.segundoApellido} onChange={manejarCambioInput} className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
              </div>

              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">Email *</label>
                <input type="email" name="email" value={formData.email} onChange={manejarCambioInput} required className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
              </div>

              <div className="grid grid-cols-2 gap-4 mb-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Contraseña {usuarioActual && <span className="text-xs text-gray-400 font-normal">(Opcional al editar)</span>}
                  </label>
                  <input type="password" name="password" value={formData.password} onChange={manejarCambioInput} required={!usuarioActual} className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Rol *</label>
                  <select name="rol" value={formData.rol} onChange={manejarCambioInput} className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white">
                    <option value="USER">USER</option>
                    <option value="ADMIN">ADMIN</option>
                  </select>
                </div>
              </div>

              <div className="flex justify-end space-x-3 border-t pt-4">
                <button type="button" onClick={cerrarModal} className="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300 transition-colors">
                  Cancelar
                </button>
                <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors">
                  {usuarioActual ? 'Guardar Cambios' : 'Crear Usuario'}
                </button>
              </div>
            </form>

          </div>
        </div>
      )}
    </div>
  );
};

export default UserList;