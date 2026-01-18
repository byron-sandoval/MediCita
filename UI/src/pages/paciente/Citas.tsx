import React, { useEffect, useState } from 'react';
import { citaService } from '../../services/citaService';
import { useAuth } from '../../context/AuthContext';
import type { Cita } from '../../types';
import { Calendar, Clock, MapPin, Search, Plus } from 'lucide-react';
import { Link } from 'react-router-dom';

const PacienteCitas: React.FC = () => {
    const { user } = useAuth();
    const [citas, setCitas] = useState<Cita[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCitas = async () => {
            try {
                const data = await citaService.getAll();
                const filter = data.filter(c => c.paciente?.keycloakId === user?.id);
                setCitas(filter);
            } catch (err) {
                console.error("Error fetching patient appointments", err);
            } finally {
                setLoading(false);
            }
        };
        fetchCitas();
    }, [user?.id]);

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div className="flex justify-between items-center">
                <div>
                    <h2 className="text-3xl font-bold text-slate-800 italic">Mis Citas Médicas</h2>
                    <p className="text-slate-500 mt-1">Consulta el estado de tus citas programadas.</p>
                </div>
                <Link
                    to="/paciente/buscar"
                    className="flex items-center px-6 py-3 bg-medical-500 text-white rounded-xl hover:bg-medical-600 shadow-lg shadow-medical-500/20 font-bold active:scale-95 transition-all"
                >
                    <Plus className="w-5 h-5 mr-2" />
                    Agendar Nueva
                </Link>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                {loading ? (
                    <div className="col-span-full py-20 flex justify-center"><div className="animate-spin rounded-full h-10 w-10 border-t-2 border-medical-500"></div></div>
                ) : citas.length === 0 ? (
                    <div className="col-span-full py-20 text-center bg-white rounded-3xl border border-dashed border-slate-200">
                        <Calendar className="w-12 h-12 text-slate-100 mx-auto mb-4" />
                        <p className="text-slate-400">No tienes citas programadas actualmente</p>
                    </div>
                ) : citas.map((cita) => (
                    <div key={cita.id} className="bg-white p-6 rounded-3xl shadow-sm border border-slate-100 hover:shadow-md transition-all group">
                        <div className="flex items-center justify-between mb-6">
                            <span className={`px-3 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider ${cita.estado === 'PENDIENTE' ? 'bg-orange-50 text-orange-600' : 'bg-emerald-50 text-emerald-600'}`}>
                                {cita.estado}
                            </span>
                            <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center">
                                <Calendar className="w-3 h-3 mr-1" />
                                {new Date(cita.fechaHora).toLocaleDateString()}
                            </p>
                        </div>

                        <div className="flex items-start gap-4 mb-6">
                            <div className="w-16 h-16 rounded-2xl bg-slate-50 flex items-center justify-center text-slate-400 group-hover:bg-medical-50 group-hover:text-medical-600 transition-colors">
                                <Search className="w-8 h-8" />
                            </div>
                            <div>
                                <h4 className="font-bold text-slate-800 text-lg uppercase tracking-tight">
                                    Dr. {cita.medico?.nombre} {cita.medico?.apellido}
                                </h4>
                                <p className="text-sm text-medical-600 font-bold">{cita.medico?.especialidad}</p>
                            </div>
                        </div>

                        <div className="grid grid-cols-2 gap-4 pt-6 border-t border-slate-50">
                            <div className="flex items-center text-slate-500">
                                <Clock className="w-4 h-4 mr-2 text-slate-300" />
                                <span className="text-xs font-bold font-mono">
                                    {new Date(cita.fechaHora).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                </span>
                            </div>
                            <div className="flex items-center text-slate-500">
                                <MapPin className="w-4 h-4 mr-2 text-slate-300" />
                                <span className="text-xs font-bold uppercase truncate">Consultorio Principal</span>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default PacienteCitas;
