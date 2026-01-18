import React from 'react';
import Sidebar from '../components/Sidebar';
import { useAuth } from '../context/AuthContext';
import { Bell, Search } from 'lucide-react';

interface LayoutProps {
    children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
    const { user } = useAuth();

    return (
        <div className="flex min-h-screen bg-slate-50">
            <Sidebar />

            <main className="flex-1 ml-64 flex flex-col">
                {/* Header */}
                <header className="h-16 bg-white border-b border-slate-200 px-8 flex items-center justify-between sticky top-0 z-40">
                    <div className="flex items-center bg-slate-100 px-4 py-2 rounded-lg w-96 transform transition-all duration-300 focus-within:ring-2 focus-within:ring-medical-500/20 focus-within:bg-white focus-within:shadow-sm">
                        <Search className="w-4 h-4 text-slate-400 mr-2" />
                        <input
                            type="text"
                            placeholder="Buscar citas, pacientes, resultados..."
                            className="bg-transparent border-none text-sm focus:outline-none w-full text-slate-600"
                        />
                    </div>

                    <div className="flex items-center space-x-6">
                        <button className="relative text-slate-500 hover:text-medical-600 transition-colors">
                            <Bell className="w-5 h-5" />
                            <span className="absolute -top-1 -right-1 w-2 h-2 bg-red-500 rounded-full border-2 border-white"></span>
                        </button>

                        <div className="flex items-center space-x-3 pl-6 border-l border-slate-200">
                            <div className="text-right">
                                <p className="text-sm font-semibold text-slate-800">{user?.name}</p>
                                <p className="text-xs font-medium text-slate-400 uppercase tracking-wider">
                                    {user?.role.replace('ROLE_', '')}
                                </p>
                            </div>
                            <div className="w-10 h-10 bg-medical-100 rounded-full flex items-center justify-center text-medical-600 font-bold border-2 border-medical-50">
                                {user?.name[0]}
                            </div>
                        </div>
                    </div>
                </header>

                {/* Page Content */}
                <div className="p-8">
                    <div className="max-w-7xl mx-auto">
                        {children}
                    </div>
                </div>
            </main>
        </div>
    );
};

export default Layout;
