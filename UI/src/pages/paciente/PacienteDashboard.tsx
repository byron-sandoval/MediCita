import React, { useEffect, useState } from 'react';
import { Calendar, Activity, TrendingUp, Clock, Plus, Stethoscope } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { citaService } from '../../services/citaService';
import { pacienteService } from '../../services/pacienteService';
import type { Cita, Paciente } from '../../types';
import { Link } from 'react-router-dom';

interface StatCardProps {
    title: string;
    value: string | number;
    trend: string;
    icon: React.ElementType;
    color: string;
}

const StatCard: React.FC<StatCardProps> = ({ title, value, trend, icon: Icon, color }) => (
    <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 hover:shadow-md transition-all duration-300">
        <div className="flex items-center justify-between mb-4">
            <div className={`p-3 rounded-xl ${color}`}>
                <Icon className="w-6 h-6 text-white" />
            </div>
            <span className="text-sm font-semibold text-slate-400 bg-slate-50 px-2 py-1 rounded-lg">
                {trend}
            </span>
        </div>
        <p className="text-slate-500 text-sm font-medium mb-1">{title}</p>
        <h3 className="text-2xl font-bold text-slate-800">{value}</h3>
    </div>
);

const PacienteDashboard: React.FC = () => {
    const { user } = useAuth();
    const [citas, setCitas] = useState<Cita[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCitas = async () => {
            try {
                setLoading(true);
                const [data, pacientes] = await Promise.all([
                    citaService.getAll(),
                    pacienteService.getAll()
                ]);

                // Sync logic
                let currentP = pacientes.find((p: Paciente) => p.keycloakId === user?.id);
                if (!currentP && user?.email) {
                    currentP = pacientes.find((p: Paciente) => p.email.toLowerCase() === user.email.toLowerCase());
                    if (currentP && currentP.id) {
                        await pacienteService.update(currentP.id, {
                            ...currentP,
                            keycloakId: user.id
                        });
                    }
                }

                const userCitas = data.filter((c: Cita) => c.paciente?.keycloakId === user?.id);
                setCitas(userCitas);
            } catch (error) {
                console.error("Error fetching dashboard data", error);
            } finally {
                setLoading(false);
            }
        };

        fetchCitas();
    }, [user?.id, user?.email]);

    const proximaCita = citas.length > 0 ? citas[0] : null;

    return (
        <div className="space-y-8 animate-in fade-in duration-500">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div>
                    <h2 className="text-3xl font-bold text-slate-800 italic">Hola, {user?.name}</h2>
                    <p className="text-slate-500 mt-1">Este es el resumen de tu salud y próximas citas.</p>
                </div>
                <Link
                    to="/paciente/buscar"
                    className="flex items-center px-6 py-3 bg-medical-500 text-white rounded-xl hover:bg-medical-600 transition-all shadow-lg shadow-medical-500/20 active:scale-95 font-bold w-fit"
                >
                    <Plus className="w-5 h-5 mr-2" />
                    Nueva Cita
                </Link>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <StatCard title="Mis Citas" value={citas.length} trend="Total" icon={Calendar} color="bg-medical-500" />
                <StatCard title="Historiales" value="0" trend="Ver" icon={Activity} color="bg-blue-500" />
                <StatCard title="Pagos" value="0" trend="Al día" icon={TrendingUp} color="bg-emerald-500" />
                <StatCard title="Avisos" value="0" trend="Nuevas" icon={Clock} color="bg-purple-500" />
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <div className="lg:col-span-2 bg-white rounded-2xl shadow-sm border border-slate-100 p-8">
                    <h4 className="text-lg font-bold text-slate-800 mb-6 flex items-center">
                        <Calendar className="w-5 h-5 mr-3 text-medical-500" />
                        Próxima Actividad
                    </h4>

                    {loading ? (
                        <div className="animate-pulse space-y-4">
                            <div className="h-20 bg-slate-50 rounded-xl"></div>
                        </div>
                    ) : proximaCita ? (
                        <div className="space-y-4">
                            <div className="flex items-center p-4 rounded-xl border border-blue-100 bg-blue-50/30">
                                <div className="w-12 h-12 rounded-xl bg-medical-500 flex items-center justify-center mr-4">
                                    <Calendar className="w-6 h-6 text-white" />
                                </div>
                                <div className="flex-1">
                                    <p className="font-bold text-slate-800 text-lg">
                                        Dr. {proximaCita.medico?.nombre} {proximaCita.medico?.apellido}
                                    </p>
                                    <p className="text-sm text-slate-500">
                                        {proximaCita.medico?.especialidad} - {new Date(proximaCita.fechaHora).toLocaleString()}
                                    </p>
                                </div>
                                <span className="px-4 py-1 bg-medical-500 text-white text-xs font-bold rounded-full uppercase">
                                    {proximaCita.estado}
                                </span>
                            </div>
                        </div>
                    ) : (
                        <div className="p-10 text-center border-2 border-dashed border-slate-100 rounded-2xl">
                            <Calendar className="w-12 h-12 text-slate-200 mx-auto mb-4" />
                            <p className="text-slate-400 font-medium">No tienes citas programadas</p>
                            <Link to="/paciente/buscar" className="text-medical-500 text-sm font-bold mt-2 inline-block hover:underline">
                                Agendar mi primera cita
                            </Link>
                        </div>
                    )}
                </div>

                <div className="bg-gradient-to-br from-indigo-600 to-indigo-700 rounded-2xl shadow-xl p-8 text-white relative overflow-hidden">
                    <div className="relative z-10">
                        <h4 className="text-xl font-bold mb-4 italic">Perfil Paciente</h4>
                        <p className="text-indigo-100 text-sm mb-6">
                            Asegúrate de tener tus datos de contacto actualizados para recibir notificaciones.
                        </p>
                        <Link
                            to="/profile"
                            className="w-full py-3 bg-white text-indigo-600 rounded-xl font-bold hover:bg-indigo-50 transition-colors flex items-center justify-center shadow-lg"
                        >
                            Completar Perfil
                        </Link>
                    </div>
                    <Stethoscope className="absolute -bottom-10 -right-10 w-48 h-48 text-indigo-500/20 transform rotate-12" />
                </div>
            </div>
        </div>
    );
};

export default PacienteDashboard;
