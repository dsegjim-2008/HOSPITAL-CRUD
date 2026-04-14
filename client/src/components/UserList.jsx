import React, { useEffect, useState } from 'react';
import Swal from 'sweetalert2';
import { getUsuarios, borrarUsuario, crearUsuario, actualizarUsuario } from '../services/userService';

// --- CONFIGURACIÓN DEL TOAST DE SWEETALERT2 ---
const Toast = Swal.mixin({
  toast: true,
  position: 'bottom-end',
  showConfirmButton: false,
  timer: 3000,
  timerProgressBar: true,
  didOpen: (toast) => {
    toast.onmouseenter = Swal.stopTimer;
    toast.onmouseleave = Swal.resumeTimer;
  }
});

const UserList = ({ usuarioSesion }) => {
  const [usuarios, setUsuarios] = useState([]);
  const [cargando, setCargando] = useState(true);
  
  const [mostrarModal, setMostrarModal] = useState(false);
  const [usuarioActual, setUsuarioActual] = useState(null);
  const [formData, setFormData] = useState({
    primerNombre: '', segundoNombre: '', primerApellido: '', segundoApellido: '', email: '', password: '', rol: 'USER'
  });

  // --- 1. CARGAR DATOS ---
  useEffect(() => {
    getUsuarios()
      .then(res => {
        setUsuarios(res.data);
        setCargando(false);
      })
      .catch(() => {
        Toast.fire({ icon: 'error', title: 'Error al cargar la base de datos' });
        setCargando(false);
      });
  }, []);

  // --- 2. BORRAR ---
  const handleBorrar = (id, nombre) => {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `Vas a eliminar a ${nombre}. No podrás deshacer esto.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc2626',
      cancelButtonColor: '#e5e7eb',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
      customClass: { cancelButton: 'text-gray-800' }
    }).then((result) => {
      if (result.isConfirmed) {
        borrarUsuario(id)
          .then(() => {
            setUsuarios(usuarios.filter(u => u.id !== id));
            Toast.fire({ icon: 'success', title: 'Usuario eliminado' });
          })
          .catch(() => Toast.fire({ icon: 'error', title: 'Error al eliminar' }));
      }
    });
  };

  // --- 3. GUARDAR (CREAR O EDITAR) ---
  const handleGuardar = (e) => {
    e.preventDefault();
    
    // Mostramos un modal de carga que bloquea la pantalla para que no hagan doble clic
    Swal.fire({
      title: 'Guardando...',
      allowOutsideClick: false,
      didOpen: () => Swal.showLoading()
    });

    const promesa = usuarioActual 
      ? actualizarUsuario(usuarioActual.id, formData)
      : crearUsuario(formData);

    promesa
      .then(res => {
        if (usuarioActual) {
          setUsuarios(usuarios.map(u => u.id === usuarioActual.id ? res.data : u));
        } else {
          setUsuarios([...usuarios, res.data]);
        }
        setMostrarModal(false);
        Swal.close(); // Cerramos el loading
        Toast.fire({ icon: 'success', title: usuarioActual ? 'Actualizado correctamente' : 'Creado con éxito' });
      })
      .catch(err => {
        Swal.fire({ // Mostramos el error en un modal grande para que se lea bien el motivo
          icon: 'error',
          title: 'Error al guardar',
          text: err.response?.data?.error || "Error desconocido",
          confirmButtonColor: '#3b82f6'
        });
      });
  };

  // --- MANEJO DEL MODAL ---
  const abrirModal = (user = null) => {
    if (user) {
      setUsuarioActual(user);
      setFormData({ ...user, password: '' }); 
    } else {
      setUsuarioActual(null);
      setFormData({ primerNombre: '', segundoNombre: '', primerApellido: '', segundoApellido: '', email: '', password: '', rol: 'USER' });
    }
    setMostrarModal(true);
  };

  if (cargando) return <div className="text-center p-10">Cargando...</div>;

  return (
    <div className="container mx-auto p-6 relative">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800">Lista de Usuarios</h2>
      </div>

      <div className="bg-white shadow-md rounded-lg overflow-hidden">
        <table className="min-w-full leading-normal">
          <thead>
            <tr className="bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
              <th className="px-5 py-3 border-b-2">Nombre Completo</th>
              <th className="px-5 py-3 border-b-2">Email</th>
              <th className="px-5 py-3 border-b-2">Rol</th>
              <th className="px-5 py-3 border-b-2 text-center">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {usuarios.map((user) => (
              <tr key={user.id} className="hover:bg-gray-50 border-b">
                <td className="px-5 py-4">{user.primerNombre} {user.primerApellido}</td>
                <td className="px-5 py-4">{user.email}</td>
                <td className="px-5 py-4">
                  <span className={`px-3 py-1 rounded-full text-xs font-bold ${user.rol === 'ADMIN' ? 'bg-red-200 text-red-900' : 'bg-green-200 text-green-900'}`}>
                    {user.rol}
                  </span>
                </td>
                <td className="px-5 py-4 text-center space-x-4">
                  {usuarioSesion.rol === 'ADMIN' ? (
                    <>
                      <button onClick={() => abrirModal(user)} className="text-indigo-600 hover:text-indigo-900 font-semibold">Editar</button>
                      
                      {/* Si el usuario de esta fila soy YO mismo, no muestro el botón de borrar */}
                      {user.id !== usuarioSesion.id ? (
                        <button onClick={() => handleBorrar(user.id, user.primerNombre)} className="text-red-600 hover:text-red-900 font-semibold">
                          Borrar
                        </button>
                      ) : (
                        <span className="text-gray-400 text-xs italic">Eres tú</span>
                      )}
                    </>
                  ) : (
                    <span className="text-gray-400 text-xs italic">Sin permisos</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* MODAL */}
      {mostrarModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
          <div className="bg-white rounded-lg shadow-xl w-full max-w-lg mx-4">
            <div className="flex justify-between items-center p-6 border-b">
              <h3 className="text-xl font-semibold">{usuarioActual ? 'Editar Usuario' : 'Crear Usuario'}</h3>
              <button onClick={() => setMostrarModal(false)} className="text-gray-400 hover:text-gray-600 text-2xl">&times;</button>
            </div>
            
            <form onSubmit={handleGuardar} className="p-6">
              <div className="grid grid-cols-2 gap-4 mb-4">
                <input type="text" placeholder="Primer Nombre *" required value={formData.primerNombre} onChange={e => setFormData({...formData, primerNombre: e.target.value})} className="border rounded px-3 py-2 w-full" />
                <input type="text" placeholder="Segundo Nombre" value={formData.segundoNombre} onChange={e => setFormData({...formData, segundoNombre: e.target.value})} className="border rounded px-3 py-2 w-full" />
              </div>
              <div className="grid grid-cols-2 gap-4 mb-4">
                <input type="text" placeholder="Primer Apellido *" required value={formData.primerApellido} onChange={e => setFormData({...formData, primerApellido: e.target.value})} className="border rounded px-3 py-2 w-full" />
                <input type="text" placeholder="Segundo Apellido" value={formData.segundoApellido} onChange={e => setFormData({...formData, segundoApellido: e.target.value})} className="border rounded px-3 py-2 w-full" />
              </div>
              <input type="email" placeholder="Email *" required value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} className="border rounded px-3 py-2 w-full mb-4" />
              
              <div className="grid grid-cols-2 gap-4 mb-6">
                <input type="password" placeholder={usuarioActual ? "Nueva contraseña (opcional)" : "Contraseña *"} required={!usuarioActual} value={formData.password} onChange={e => setFormData({...formData, password: e.target.value})} className="border rounded px-3 py-2 w-full" />
                <select value={formData.rol} onChange={e => setFormData({...formData, rol: e.target.value})} className="border rounded px-3 py-2 w-full bg-white">
                  <option value="USER">USER</option>
                  <option value="ADMIN">ADMIN</option>
                </select>
              </div>

              <div className="flex justify-end space-x-3 border-t pt-4">
                <button type="button" onClick={() => setMostrarModal(false)} className="px-4 py-2 bg-gray-200 text-gray-800 rounded">Cancelar</button>
                <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded">{usuarioActual ? 'Guardar Cambios' : 'Crear Usuario'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserList;