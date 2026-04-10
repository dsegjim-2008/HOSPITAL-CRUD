import React from 'react';
import UserList from './components/UserList';

function App() {
  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-gray-800 p-4 shadow-lg">
        <h1 className="text-white text-xl font-bold">Admin Panel Empresarial</h1>
      </nav>
      <main>
        <UserList />
      </main>
    </div>
  );
}

export default App;