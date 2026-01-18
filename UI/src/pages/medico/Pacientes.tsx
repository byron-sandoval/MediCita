import React, { useEffect, useState } from 'react';
import { pacienteService } from '../../services/pacienteService';
import { citaService } from '../../services/citaService';
import { useAuth } from '../../context/AuthContext';
import type { Paciente } from '../../types';
import { Search, Users, ExternalLink, MessageCircle } from 'lucide-react';

const MedicoPacientes: React.FC = () => {
    const { user } = useAuth();
    const [pacientes, setPacientes] = useState<Paciente[]>([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        const fetchMisPacientes = async () => {
            try {
                setLoading(true);
                // En un sistema real, el backend filtraría pacientes que han tenido cita con este médico.
                // Por ahora, como demo, mostramos todos los pacientes activos.
                const data = await pacienteService.getAll();
                setPacientes(data.filter(p => p.activo));
            } catch (error) {
                console.error('Error fetching medico patients:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchMisPacientes();
    }, []);

    const filtered = pacientes.filter(p =>
        `${p.nombre} ${p.apellido}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
        p.cedula.includes(searchTerm)
    );

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div>
                <h2 className="text-3xl font-bold text-slate-800 italic">Mis Pacientes</h2>
                <p className="text-slate-500 mt-1">Directorio de pacientes que han consultado contigo.</p>
            </div>

            <div className="relative">
                <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 w-5 h-5" />
                <input
                    type="text"
                    placeholder="Buscar por nombre o cédula..."
                    className="w-full pl-12 pr-6 py-4 bg-white border border-slate-100 rounded-2xl shadow-sm focus:outline-none focus:ring-2 focus:ring-medical-500/20 transition-all text-slate-600"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {loading ? (
                    <div className="col-span-full flex justify-center py-20">
                        <div className="animate-spin rounded-full h-10 w-10 border-t-2 border-medical-500"></div>
                    </div>
                ) : filtered.length === 0 ? (
                    <div className="col-span-full py-20 text-center bg-white rounded-3xl border border-dashed border-slate-200">
                        <Users className="w-12 h-12 text-slate-100 mx-auto mb-4" />
                        <p className="text-slate-400">No se encontraron pacientes registrados</p>
                    </div>
                ) : filtered.map((p) => (
                    <div key={p.id} className="bg-white p-6 rounded-3xl shadow-sm border border-slate-100 hover:shadow-md transition-all group">
                        <div className="flex items-center gap-4 mb-6">
                            <div className="w-14 h-14 rounded-2xl bg-medical-500 text-white flex items-center justify-center font-bold text-xl shadow-lg shadow-medical-500/20">
                                {p.nombre[0]}
                            </div>
                            <div>
                                <h4 className="font-bold text-slate-800 uppercase tracking-tight">{p.nombre} {p.apellido}</h4>
                                <p className="text-xs text-slate-400 font-mono">{p.cedula}</p>
                            </div>
                        </div>

                        <div className="space-y-3 mb-6">
                            <div className="flex justify-between text-xs">
                                <span className="text-slate-400 font-bold uppercase tracking-widest">Email</span>
                                <span className="text-slate-600 font-medium">{p.email}</span>
                            </div>
                            <div className="flex justify-between text-xs">
                                <span className="text-slate-400 font-bold uppercase tracking-widest">Teléfono</span>
                                <span className="text-slate-600 font-medium">{p.telefono}</span>
                            </div>
                        </div>

                        <div className="flex gap-2">
                            <button className="flex-1 flex items-center justify-center gap-2 py-2 bg-slate-50 text-slate-600 rounded-xl text-xs font-bold hover:bg-medical-50 hover:text-medical-600 transition-colors">
                                <MessageCircle className="w-4 h-4" />
                                Chat
                            </button>
                            <button className="flex-1 flex items-center justify-center gap-2 py-2 bg-medical-500 text-white rounded-xl text-xs font-bold hover:bg-medical-600 transition-all">
                                <ExternalLink className="w-4 h-4" />
                                Expediente
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MedicoPacientes;
