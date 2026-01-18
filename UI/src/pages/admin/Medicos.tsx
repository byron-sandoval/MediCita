import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { medicoService } from '../../services/medicoService';
import { Especialidad, type Medico } from '../../types';
import { Users, Search, Plus, Filter, MoreVertical, Trash2, Edit, X, Stethoscope } from 'lucide-react';
import { toast } from 'sonner';

const AdminMedicos: React.FC = () => {
    const [medicos, setMedicos] = useState<Medico[]>([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [saving, setSaving] = useState(false);

    const { register, handleSubmit, reset, formState: { errors } } = useForm<Partial<Medico>>();

    useEffect(() => {
        fetchMedicos();
    }, []);

    const fetchMedicos = async () => {
        try {
            setLoading(true);
            const data = await medicoService.getAll();
            setMedicos(data);
        } catch (error) {
            console.error('Error fetching medicos:', error);
            toast.error('No se pudo conectar con el servidor.');
        } finally {
            setLoading(false);
        }
    };

    const onSubmit = async (data: Partial<Medico>) => {
        try {
            setSaving(true);
            const newMedico = {
                ...data,
                activo: true,
                keycloakId: 'pending',
                tarifaConsulta: Number(data.tarifaConsulta)
            };
            await medicoService.create(newMedico);
            toast.success('Médico registrado con éxito');
            setIsModalOpen(false);
            reset();
            fetchMedicos();
        } catch (error) {
            console.error('Error creating medico:', error);
            toast.error('Error al guardar el médico. Revisa los datos.');
        } finally {
            setSaving(false);
        }
    };

    const filteredMedicos = medicos.filter(m =>
        `${m.nombre} ${m.apellido}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
        m.numeroLicencia.includes(searchTerm) ||
        m.especialidad.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div>
                    <h2 className="text-3xl font-bold text-slate-800 italic">Gestión de Médicos</h2>
                    <p className="text-slate-500 mt-1">Administra el personal médico y sus especialidades.</p>
                </div>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="flex items-center px-6 py-3 bg-medical-500 text-white rounded-xl hover:bg-medical-600 transition-all shadow-lg shadow-medical-500/20 font-bold active:scale-95"
                >
                    <Plus className="w-5 h-5 mr-2" />
                    Nuevo Médico
                </button>
            </div>

            {/* Toolbar */}
            <div className="bg-white p-4 rounded-2xl shadow-sm border border-slate-100 flex flex-col md:flex-row gap-4">
                <div className="flex-1 relative">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 w-5 h-5" />
                    <input
                        type="text"
                        placeholder="Buscar por nombre, licencia o especialidad..."
                        className="w-full pl-10 pr-4 py-2 bg-slate-50 border border-slate-100 rounded-xl focus:outline-none focus:ring-2 focus:ring-medical-500/20 focus:bg-white transition-all"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
                <button className="flex items-center px-4 py-2 text-slate-600 bg-slate-50 border border-slate-100 rounded-xl hover:bg-slate-100 transition-all font-medium">
                    <Filter className="w-4 h-4 mr-2" />
                    Filtros
                </button>
            </div>

            {/* Table */}
            <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
                {loading ? (
                    <div className="p-20 flex flex-col items-center justify-center space-y-4">
                        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-medical-500"></div>
                        <p className="text-slate-400 font-medium">Cargando personal médico...</p>
                    </div>
                ) : filteredMedicos.length === 0 ? (
                    <div className="p-20 flex flex-col items-center justify-center text-center space-y-4">
                        <div className="w-20 h-20 bg-slate-50 rounded-full flex items-center justify-center">
                            <Stethoscope className="w-10 h-10 text-slate-200" />
                        </div>
                        <div>
                            <p className="text-lg font-bold text-slate-800">No hay médicos registrados</p>
                            <p className="text-slate-500">Haz clic en '+ Nuevo Médico' para comenzar.</p>
                        </div>
                    </div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="w-full text-left">
                            <thead>
                                <tr className="bg-slate-50 border-b border-slate-100">
                                    <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider">Médico</th>
                                    <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider">Especialidad</th>
                                    <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider">Licencia</th>
                                    <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider">Costo Consulta</th>
                                    <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider text-right">Acciones</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-50">
                                {filteredMedicos.map((medico) => (
                                    <tr key={medico.id} className="hover:bg-slate-50/50 transition-colors group">
                                        <td className="px-6 py-4">
                                            <div className="flex items-center">
                                                <div className="w-10 h-10 rounded-full bg-blue-50 text-blue-600 flex items-center justify-center font-bold mr-3 border border-blue-100">
                                                    {medico.nombre[0]}
                                                </div>
                                                <div>
                                                    <p className="font-bold text-slate-800">Dr. {medico.nombre} {medico.apellido}</p>
                                                    <p className="text-xs text-slate-400">{medico.email}</p>
                                                </div>
                                            </div>
                                        </td>
                                        <td className="px-6 py-4">
                                            <span className="px-3 py-1 bg-medical-50 text-medical-600 rounded-lg text-xs font-bold uppercase transition-all group-hover:bg-medical-500 group-hover:text-white">
                                                {medico.especialidad}
                                            </span>
                                        </td>
                                        <td className="px-6 py-4 text-sm font-mono font-bold text-slate-500">
                                            {medico.numeroLicencia}
                                        </td>
                                        <td className="px-6 py-4">
                                            <p className="text-sm font-bold text-emerald-600">${medico.tarifaConsulta}</p>
                                        </td>
                                        <td className="px-6 py-4 text-right">
                                            <div className="flex items-center justify-end space-x-2 opacity-0 group-hover:opacity-100 transition-opacity">
                                                <button className="p-2 text-slate-400 hover:text-medical-600 hover:bg-medical-50 rounded-lg transition-all">
                                                    <Edit className="w-4 h-4" />
                                                </button>
                                                <button className="p-2 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all">
                                                    <Trash2 className="w-4 h-4" />
                                                </button>
                                                <button className="p-2 text-slate-400 hover:text-slate-600 rounded-lg">
                                                    <MoreVertical className="w-4 h-4" />
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            {/* Modal para Nuevo Médico */}
            {isModalOpen && (
                <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-in fade-in duration-300">
                    <div className="bg-white w-full max-w-2xl rounded-3xl shadow-2xl overflow-hidden border border-slate-100 animate-in zoom-in-95 duration-300">
                        <div className="flex items-center justify-between p-8 border-b border-slate-50 bg-slate-50/50">
                            <div>
                                <h3 className="text-2xl font-bold text-slate-800 italic">Registrar Nuevo Médico</h3>
                                <p className="text-sm text-slate-500">Asigna especialidad y credenciales</p>
                            </div>
                            <button
                                onClick={() => setIsModalOpen(false)}
                                className="p-2 hover:bg-white rounded-full text-slate-400 hover:text-slate-600 transition-colors shadow-sm"
                            >
                                <X className="w-6 h-6" />
                            </button>
                        </div>

                        <form onSubmit={handleSubmit(onSubmit)} className="p-8 space-y-6">
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Nombre</label>
                                    <input
                                        {...register('nombre', { required: true })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white"
                                        placeholder="Ej. Gregory"
                                    />
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Apellido</label>
                                    <input
                                        {...register('apellido', { required: true })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white"
                                        placeholder="Ej. House"
                                    />
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Email Profesional</label>
                                    <input
                                        {...register('email', { required: true, pattern: /^\S+@\S+$/i })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white"
                                        placeholder="house@medicita.com"
                                    />
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Número de Licencia</label>
                                    <input
                                        {...register('numeroLicencia', { required: true })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white"
                                        placeholder="LIC-000000"
                                    />
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Especialidad</label>
                                    <select
                                        {...register('especialidad', { required: true })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white appearance-none"
                                    >
                                        {Object.values(Especialidad).map((esp) => (
                                            <option key={esp} value={esp}>{esp.replace('_', ' ')}</option>
                                        ))}
                                    </select>
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Tarifa Consulta ($)</label>
                                    <input
                                        type="number"
                                        step="0.01"
                                        {...register('tarifaConsulta', { required: true, min: 0 })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white"
                                        placeholder="50.00"
                                    />
                                </div>
                            </div>

                            <div className="pt-6 flex gap-4">
                                <button
                                    type="button"
                                    onClick={() => setIsModalOpen(false)}
                                    className="flex-1 py-4 text-slate-500 font-bold hover:bg-slate-50 rounded-2xl transition-all"
                                >
                                    Cerrar
                                </button>
                                <button
                                    type="submit"
                                    disabled={saving}
                                    className="flex-1 py-4 bg-medical-500 text-white rounded-2xl font-bold hover:bg-medical-600 transition-all shadow-lg shadow-medical-500/20 disabled:bg-slate-200 disabled:shadow-none flex items-center justify-center font-serif italic"
                                >
                                    {saving ? (
                                        <div className="w-6 h-6 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
                                    ) : (
                                        'Inscribir Médico'
                                    )}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminMedicos;
