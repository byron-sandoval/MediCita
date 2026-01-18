import React from 'react';
import {
    Users,
    Calendar,
    Stethoscope,
    FileText,
    Settings,
    LogOut,
    ChevronRight,
    LayoutDashboard,
    UserCircle
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { cn } from '../utils/cn';
import { Link, useLocation } from 'react-router-dom';

interface SidebarItemProps {
    icon: React.ElementType;
    label: string;
    to: string;
}

const SidebarItem: React.FC<SidebarItemProps> = ({ icon: Icon, label, to }) => {
    const location = useLocation();
    const active = location.pathname === to;

    return (
        <Link
            to={to}
            className={cn(
                "flex items-center w-full px-4 py-3 text-sm font-medium transition-all duration-200 rounded-lg group text-left",
                active
                    ? "bg-medical-500 text-white shadow-md shadow-medical-500/20"
                    : "text-slate-600 hover:bg-medical-50 hover:text-medical-600"
            )}
        >
            <Icon className={cn("w-5 h-5 mr-3", active ? "text-white" : "text-slate-400 group-hover:text-medical-500")} />
            <span className="flex-1">{label}</span>
            {active && <ChevronRight className="w-4 h-4 ml-auto" />}
        </Link>
    );
};

const Sidebar: React.FC = () => {
    const { user, logout } = useAuth();
    const role = user?.role;

    return (
        <aside className="fixed left-0 top-0 h-screen w-64 bg-white border-r border-slate-200 flex flex-col p-4 z-50">
            <div className="flex items-center px-4 py-6 mb-8">
                <div className="w-10 h-10 bg-medical-500 rounded-xl flex items-center justify-center mr-3 shadow-lg shadow-medical-500/30">
                    <Stethoscope className="text-white w-6 h-6" />
                </div>
                <h1 className="text-xl font-bold bg-gradient-to-r from-medical-600 to-medical-400 bg-clip-text text-transparent">
                    MediCita
                </h1>
            </div>

            <nav className="flex-1 space-y-2">
                <SidebarItem icon={LayoutDashboard} label="Dashboard" to="/dashboard" />

                {/* ROLE_ADMIN Options */}
                {role === 'ROLE_ADMIN' && (
                    <>
                        <SidebarItem icon={Users} label="Gestión Pacientes" to="/admin/pacientes" />
                        <SidebarItem icon={Users} label="Gestión Médicos" to="/admin/medicos" />
                        <SidebarItem icon={Stethoscope} label="Especialidades" to="/admin/especialidades" />
                        <SidebarItem icon={Settings} label="Contenido Web" to="/admin/configuracion" />
                    </>
                )}

                {/* ROLE_MEDICO Options */}
                {role === 'ROLE_MEDICO' && (
                    <>
                        <SidebarItem icon={Calendar} label="Mi Agenda" to="/medico/agenda" />
                        <SidebarItem icon={Users} label="Mis Pacientes" to="/medico/pacientes" />
                        <SidebarItem icon={FileText} label="Historiales" to="/medico/historias" />
                    </>
                )}

                {/* ROLE_USER Options */}
                {role === 'ROLE_USER' && (
                    <>
                        <SidebarItem icon={Calendar} label="Mis Citas" to="/paciente/citas" />
                        <SidebarItem icon={FileText} label="Mi Historial" to="/paciente/historial" />
                        <SidebarItem icon={Users} label="Buscar Médico" to="/paciente/buscar" />
                    </>
                )}

                <div className="pt-4 mt-4 border-t border-slate-100">
                    <SidebarItem icon={UserCircle} label="Configuración Perfil" to="/profile" />
                </div>
            </nav>

            <div className="mt-auto">
                <button
                    onClick={logout}
                    className="flex items-center w-full px-4 py-3 text-sm font-medium text-slate-500 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors duration-200"
                >
                    <LogOut className="w-5 h-5 mr-3" />
                    Cerrar Sesión
                </button>
            </div>
        </aside>
    );
};

export default Sidebar;
