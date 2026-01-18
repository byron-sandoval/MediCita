import React, { useEffect, useState } from 'react';
import { Users, Clock, CheckCircle2, Activity, Calendar } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { citaService } from '../../services/citaService';
import type { Cita } from '../../types';

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
            <span className="text-sm font-semibold text-emerald-500 bg-emerald-50 px-2 py-1 rounded-lg">
                {trend}
            </span>
        </div>
        <p className="text-slate-500 text-sm font-medium mb-1">{title}</p>
        <h3 className="text-2xl font-bold text-slate-800">{value}</h3>
    </div>
);

const MedicoDashboard: React.FC = () => {
    const { user } = useAuth();
    const [citas, setCitas] = useState<Cita[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchMisCitas = async () => {
            try {
                const data = await citaService.getAll();
                // En un caso real filtraríamos por ID del médico logueado
                const misCitas = data.filter(c => c.medico?.email === user?.email);
                setCitas(misCitas);
            } catch (err) {
                console.error("Error cargando agenda médica", err);
            } finally {
                setLoading(false);
            }
        };
        fetchMisCitas();
    }, [user?.email]);

    return (
        <div className="space-y-8 animate-in fade-in duration-500">
            <div>
                <h2 className="text-3xl font-bold text-slate-800 italic">Bienvenido, {user?.name}</h2>
                <p className="text-slate-500 mt-1">Esta es tu agenda y resumen de pacientes.</p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <StatCard title="Pacientes" value={citas.length} trend="Total" icon={Users} color="bg-blue-500" />
                <StatCard title="Pendientes" value={citas.filter(c => c.estado === 'PENDIENTE').length} trend="Por ver" icon={Clock} color="bg-orange-500" />
                <StatCard title="Completadas" value={citas.filter(c => c.estado === 'COMPLETADA').length} trend="Hoy" icon={CheckCircle2} color="bg-emerald-500" />
                <StatCard title="Promedio" value="4.9" trend="Rating" icon={Activity} color="bg-purple-500" />
            </div>

            <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-8">
                <h4 className="text-lg font-bold text-slate-800 mb-6 flex items-center">
                    <Calendar className="w-5 h-5 mr-3 text-medical-500" />
                    Agenda de Próximos Pacientes
                </h4>

                {loading ? (
                    <div className="flex justify-center p-10">
                        <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-medical-500"></div>
                    </div>
                ) : citas.length > 0 ? (
                    <div className="space-y-4">
                        {citas.map((cita) => (
                            <div key={cita.id} className="flex items-center p-4 rounded-xl border border-slate-50 hover:bg-slate-50 transition-colors group">
                                <div className="w-12 h-12 rounded-xl bg-slate-100 group-hover:bg-medical-100 flex items-center justify-center mr-4 transition-colors">
                                    <Users className="w-6 h-6 text-slate-400 group-hover:text-medical-600" />
                                </div>
                                <div className="flex-1">
                                    <p className="font-semibold text-slate-800">
                                        {cita.paciente?.nombre} {cita.paciente?.apellido}
                                    </p>
                                    <p className="text-sm text-slate-500">
                                        {cita.motivo} - {new Date(cita.fechaHora).toLocaleTimeString()}
                                    </p>
                                </div>
                                <span className={`px-3 py-1 rounded-full text-[10px] font-bold uppercase ${cita.estado === 'PENDIENTE' ? 'bg-orange-50 text-orange-600' : 'bg-emerald-50 text-emerald-600'}`}>
                                    {cita.estado}
                                </span>
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="text-center p-10 border-2 border-dashed border-slate-100 rounded-2xl">
                        <Calendar className="w-12 h-12 text-slate-200 mx-auto mb-4" />
                        <p className="text-slate-400 font-medium">No tienes pacientes en agenda para hoy</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default MedicoDashboard;
