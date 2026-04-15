import React, { useState } from 'react';
import { Users, Stethoscope, Calendar, LogOut, HeartPulse, AlertTriangle } from 'lucide-react';
import Login from './components/Login';
import PacienteList from './components/PacienteList';
import MedicoList from './components/MedicoList';
import HuerfanosList from './components/HuerfanosList';
import CalendarioCitas from './components/CalendarioCitas';

const SESION_KEY = 'hospitalos_sesion';

function App() {
  const [sesion, setSesion] = useState(() => {
    try {
      const guardada = sessionStorage.getItem(SESION_KEY);
      return guardada ? JSON.parse(guardada) : null;
    } catch {
      return null;
    }
  });
  const [vista, setVista] = useState('dashboard');

  const handleLogin = (nuevaSesion) => {
    sessionStorage.setItem(SESION_KEY, JSON.stringify(nuevaSesion));
    setSesion(nuevaSesion);
  };

  const handleLogout = () => {
    sessionStorage.removeItem(SESION_KEY);
    setSesion(null);
    setVista('dashboard');
  };

  if (!sesion) return <Login onLogin={handleLogin} />;

  const esAdmin = sesion.rol === 'admin';

  // Función para obtener el nombre a mostrar (evita errores de campos nulos)
  const nombreUsuario = sesion.data?.nombreCompleto || sesion.data?.primerNombre || "Usuario";
  const inicial = nombreUsuario.charAt(0).toUpperCase();

  return (
    <div className="min-h-screen bg-slate-50 flex">
      
      {/* SIDEBAR: Siempre visible, nunca cambia independientemente de la vista */}
      <aside className="w-72 bg-slate-900 text-white flex flex-col p-6 shadow-2xl sticky top-0 h-screen">
        
        <div className="mb-10 px-2 flex items-center gap-3">
            <HeartPulse className="text-indigo-400" size={32} />
            <div>
                <h2 className="text-2xl font-black leading-none">HospitalOS</h2>
                <span className="text-[10px] text-indigo-400 uppercase font-black tracking-[0.2em]">
                    {sesion.rol} Panel
                </span>
            </div>
        </div>

        <nav className="flex-1 space-y-2">
          {esAdmin ? (
            <>
              <MenuBtn act={vista==='medicos'} onClick={()=>setVista('medicos')} ico={Stethoscope} txt="Personal Médico" />
              <MenuBtn act={vista==='huerfanos'} onClick={()=>setVista('huerfanos')} ico={AlertTriangle} txt="Pacientes Huérfanos" />
            </>
          ) : (
            <>
              <MenuBtn act={vista==='agenda'} onClick={()=>setVista('agenda')} ico={Calendar} txt="Mi Agenda" />
              <MenuBtn act={vista==='mis-pacientes'} onClick={()=>setVista('mis-pacientes')} ico={Users} txt="Mis Pacientes" />
            </>
          )}
        </nav>

        {/* FOOTER DEL SIDEBAR: El botón de cerrar sesión está AQUÍ, globalmente */}
        <div className="mt-auto pt-6 border-t border-slate-800">
            <div className="flex items-center gap-3 px-2">
                <div className="w-11 h-11 bg-indigo-600 rounded-2xl flex items-center justify-center font-black text-xl shadow-lg shadow-indigo-500/20">
                    {inicial}
                </div>
                <div className="flex-1 overflow-hidden">
                    <p className="text-sm font-bold truncate text-slate-100">{nombreUsuario}</p>
                    <button 
                        onClick={handleLogout} 
                        className="text-[11px] text-red-400 flex items-center gap-1.5 hover:text-red-300 font-bold transition-colors uppercase tracking-wider"
                    >
                        <LogOut size={12}/> Cerrar Sesión
                    </button>
                </div>
            </div>
        </div>
      </aside>

      {/* CONTENIDO PRINCIPAL: Cambia según la vista, pero el sidebar no se mueve */}
      <main className="flex-1 p-10 overflow-y-auto">
        <div className="max-w-6xl mx-auto">
            {vista === 'medicos' && <MedicoList />}
            {vista === 'huerfanos' && <HuerfanosList />}
            
            {/* Si es médico, le pasamos su ID para que PacienteList sepa filtrar */}
            {vista === 'mis-pacientes' && (
                <PacienteList medicoId={sesion.rol === 'medico' ? sesion.data.id : null} />
            )}
            
            {vista === 'agenda' && <CalendarioCitas medicoId={sesion.data.id} />}
            
            {/* Pantalla de bienvenida por defecto */}
            {vista === 'dashboard' && (
                <div className="text-center py-20">
                    <h2 className="text-4xl font-black text-slate-800">Bienvenido de nuevo, {nombreUsuario}</h2>
                    <p className="text-slate-400 mt-2">Selecciona una opción del menú lateral para comenzar.</p>
                </div>
            )}
        </div>
      </main>
    </div>
  );
}

const MenuBtn = ({ act, onClick, ico: Icono, txt }) => (
    <button onClick={onClick} className={`w-full flex items-center gap-4 p-4 rounded-2xl font-bold transition-all duration-200 ${act ? 'bg-indigo-600 text-white shadow-xl shadow-indigo-600/20' : 'text-slate-400 hover:bg-slate-800 hover:text-slate-200'}`}>
        <Icono size={22} /> {txt}
    </button>
);

export default App;