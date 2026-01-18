import React, { useEffect, useState } from 'react';
import { Users, Calendar, TrendingUp, Activity, CheckCircle2 } from 'lucide-react';
import { pacienteService } from '../../services/pacienteService';
import { medicoService } from '../../services/medicoService';
import { citaService } from '../../services/citaService';

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

const AdminDashboard: React.FC = () => {
    const [stats, setStats] = useState({
        pacientes: 0,
        medicos: 0,
        citas: 0
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const [p, m, c] = await Promise.all([
                    pacienteService.getAll().catch(() => []),
                    medicoService.getAll().catch(() => []),
                    citaService.getAll().catch(() => [])
                ]);
                setStats({
                    pacientes: p.length,
                    medicos: m.length,
                    citas: c.length
                });
            } catch (err) {
                console.error("Error fetching admin stats", err);
            } finally {
                setLoading(false);
            }
        };
        fetchStats();
    }, []);

    return (
        <div className="space-y-8 animate-in fade-in duration-500">
            <div>
                <h2 className="text-3xl font-bold text-slate-800 italic">Panel Administrativo</h2>
                <p className="text-slate-500 mt-1">Gestión global del sistema MediCita.</p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <StatCard title="Total Médicos" value={loading ? '...' : stats.medicos} trend="Activos" icon={Users} color="bg-blue-500" />
                <StatCard title="Total Pacientes" value={loading ? '...' : stats.pacientes} trend="Registrados" icon={Users} color="bg-purple-500" />
                <StatCard title="Citas Totales" value={loading ? '...' : stats.citas} trend="Histórico" icon={Calendar} color="bg-medical-500" />
                <StatCard title="Estado Sistema" value="100%" trend="Online" icon={CheckCircle2} color="bg-emerald-500" />
            </div>

            <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-8">
                <h4 className="text-lg font-bold text-slate-800 mb-6 flex items-center">
                    <Activity className="w-5 h-5 mr-3 text-medical-500" />
                    Monitoreo de Infraestructura
                </h4>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="p-4 rounded-xl bg-slate-50 border border-slate-100">
                        <p className="text-sm font-medium text-slate-500">Servidor API (Spring Boot)</p>
                        <p className="text-lg font-bold text-emerald-600">Funcionando</p>
                    </div>
                    <div className="p-4 rounded-xl bg-slate-50 border border-slate-100">
                        <p className="text-sm font-medium text-slate-500">Autenticación (Keycloak)</p>
                        <p className="text-lg font-bold text-emerald-600">Seguro</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;
