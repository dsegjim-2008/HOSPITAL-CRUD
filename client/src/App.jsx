import React from 'react';
import UserList from './components/UserList';

function App() {
  return (
    <div className="min-h-screen">
      <nav className="bg-gray-800 p-4 shadow-lg">
        <h1 className="text-white text-xl font-bold">Empleados NaturalSoft</h1>
      </nav>
      
      {/* Aquí cargamos nuestra tabla */}
      <main>
        <UserList />
      </main>
    </div>
  );
}

export default App;