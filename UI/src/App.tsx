import React from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import Layout from './layouts/DashboardLayout';
import Dashboard from './pages/Dashboard';
import { Toaster } from 'sonner';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import AdminPacientes from './pages/admin/Pacientes';
import AdminMedicos from './pages/admin/Medicos';
import AdminConfiguracion from './pages/admin/Configuracion';
import MedicoAgenda from './pages/medico/Agenda';
import MedicoPacientes from './pages/medico/Pacientes';
import MedicoHistorias from './pages/medico/Historias';
import PacienteCitas from './pages/paciente/Citas';
import PacienteHistorial from './pages/paciente/Historial';
import PacienteBuscar from './pages/paciente/Buscar';
import PlaceholderPage from './pages/PlaceholderPage';

const AppContent: React.FC = () => {
  const { isAuthenticated, login, keycloakReady } = useAuth();

  // Show loading while keycloak initializes
  if (!keycloakReady) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-50">
        <div className="flex flex-col items-center space-y-4">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-medical-500"></div>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-slate-50 flex flex-col items-center justify-center p-4">
        <div className="bg-white p-10 rounded-3xl shadow-xl w-full max-w-md border border-slate-100 animate-in zoom-in duration-500 text-center">
          <div className="flex justify-center mb-8">
            <div className="w-16 h-16 bg-medical-500 rounded-2xl flex items-center justify-center shadow-lg shadow-medical-500/30">
              <span className="text-white text-3xl font-bold font-serif">+</span>
            </div>
          </div>
          <h1 className="text-2xl font-bold text-slate-800 mb-2">MediCita Portal</h1>
          <p className="text-slate-500 mb-10">Accede de forma segura a tu cuenta médica</p>

          <button
            onClick={() => login()}
            className="w-full py-4 bg-medical-500 text-white rounded-2xl font-bold hover:bg-medical-600 transition-all shadow-lg shadow-medical-500/20 active:scale-95 flex items-center justify-center"
          >
            Iniciar Sesión con Keycloak
          </button>

          <p className="mt-8 text-[10px] text-slate-300 uppercase tracking-widest font-bold">
            Clinic OS - 2026 Alpha Build
          </p>
        </div>
      </div>
    );
  }

  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard" element={<Dashboard />} />

        {/* Admin Routes */}
        <Route path="/admin/pacientes" element={<AdminPacientes />} />
        <Route path="/admin/medicos" element={<AdminMedicos />} />
        <Route path="/admin/especialidades" element={<PlaceholderPage title="Especialidades (Enum Backend)" />} />
        <Route path="/admin/configuracion" element={<AdminConfiguracion />} />

        {/* Medico Routes */}
        <Route path="/medico/agenda" element={<MedicoAgenda />} />
        <Route path="/medico/pacientes" element={<MedicoPacientes />} />
        <Route path="/medico/historias" element={<MedicoHistorias />} />

        {/* Paciente Routes */}
        <Route path="/paciente/citas" element={<PacienteCitas />} />
        <Route path="/paciente/historial" element={<PacienteHistorial />} />
        <Route path="/paciente/buscar" element={<PacienteBuscar />} />

        {/* User Profile */}
        <Route path="/profile" element={<PlaceholderPage title="Configuración de Perfil" />} />

        <Route path="*" element={<Dashboard />} />
      </Routes>
    </Layout>
  );
};

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppContent />
        <Toaster position="top-right" richColors closeButton />
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
