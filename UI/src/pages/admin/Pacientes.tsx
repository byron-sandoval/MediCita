import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { pacienteService } from '../../services/pacienteService';
import type { Paciente, Genero } from '../../types';
import { Users, Search, Plus, Filter, MoreVertical, Trash2, Edit, X } from 'lucide-react';
import { toast } from 'sonner';

const Pacientes: React.FC = () => {
    const [pacientes, setPacientes] = useState<Paciente[]>([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [saving, setSaving] = useState(false);

    const { register, handleSubmit, reset, formState: { errors } } = useForm<Partial<Paciente>>();

    useEffect(() => {
        fetchPacientes();
    }, []);

    const fetchPacientes = async () => {
        try {
            setLoading(true);
            const data = await pacienteService.getAll();
            setPacientes(data);
        } catch (error) {
            console.error('Error fetching pacientes:', error);
            toast.error('No se pudo conectar con el servidor.');
        } finally {
            setLoading(false);
        }
    };

    const onSubmit = async (data: Partial<Paciente>) => {
        try {
            setSaving(true);
            // Default values for a new patient
            const newPaciente = {
                ...data,
                activo: true,
                keycloakId: 'pending', // In a real flow, this might come from Keycloak registration
            };
            await pacienteService.create(newPaciente);
            toast.success('Paciente registrado con éxito');
            setIsModalOpen(false);
            reset();
            fetchPacientes();
        } catch (error) {
            console.error('Error creating paciente:', error);
            toast.error('Error al guardar el paciente. Revisa los datos.');
        } finally {
            setSaving(false);
        }
    };

    const filteredPacientes = pacientes.filter(p =>
        `${p.nombre} ${p.apellido}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
        p.cedula.includes(searchTerm)
    );

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div>
                    <h2 className="text-3xl font-bold text-slate-800 italic">Directorio de Pacientes</h2>
                    <p className="text-slate-500 mt-1">Gestiona la información y expedientes de los pacientes.</p>
                </div>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="flex items-center px-6 py-3 bg-medical-500 text-white rounded-xl hover:bg-medical-600 transition-all shadow-lg shadow-medical-500/20 font-bold active:scale-95"
                >
                    <Plus className="w-5 h-5 mr-2" />
                    Nuevo Paciente
                </button>
            </div>

            {/* Toolbar */}
            <div className="bg-white p-4 rounded-2xl shadow-sm border border-slate-100 flex flex-col md:flex-row gap-4">
                <div className="flex-1 relative">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 w-5 h-5" />
                    <input
                        type="text"
                        placeholder="Buscar por nombre o cédula..."
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
                        <p className="text-slate-400 font-medium">Sincronizando con el servidor...</p>
                    </div>
                ) : filteredPacientes.length === 0 ? (
                    <div className="p-20 flex flex-col items-center justify-center text-center space-y-4">
                        <div className="w-20 h-20 bg-slate-50 rounded-full flex items-center justify-center border border-slate-100">
                            <Users className="w-10 h-10 text-slate-200" />
                        </div>
                        <div>
                            <p className="text-lg font-bold text-slate-800">No hay pacientes registrados</p>
                            <p className="text-slate-500 max-w-xs mx-auto">Comienza agregando tu primer paciente usando el botón de arriba.</p>
                        </div>
                    </div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="w-full text-left">
                            <thead>
                                <tr className="bg-slate-50 border-b border-slate-100">
                                    <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider">Paciente</th>
                                    <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider">Identificación</th>
                                    <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider">Contacto</th>
                                    <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider">Estado</th>
                                    <th className="px-6 py-4 text-xs font-bold text-slate-400 uppercase tracking-wider text-right">Acciones</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-50">
                                {filteredPacientes.map((paciente) => (
                                    <tr key={paciente.id} className="hover:bg-slate-50/50 transition-colors group">
                                        <td className="px-6 py-4">
                                            <div className="flex items-center">
                                                <div className="w-10 h-10 rounded-full bg-medical-100 text-medical-600 flex items-center justify-center font-bold mr-3 border-2 border-white shadow-sm">
                                                    {paciente.nombre[0]}
                                                </div>
                                                <div>
                                                    <p className="font-bold text-slate-800 uppercase text-xs">{paciente.nombre} {paciente.apellido}</p>
                                                    <p className="text-[10px] text-slate-400 font-bold tracking-tighter">{paciente.genero}</p>
                                                </div>
                                            </div>
                                        </td>
                                        <td className="px-6 py-4">
                                            <p className="text-sm text-slate-600 font-mono font-bold bg-slate-100 px-2 py-1 rounded inline-block">{paciente.cedula}</p>
                                        </td>
                                        <td className="px-6 py-4">
                                            <p className="text-xs text-slate-600 font-medium">{paciente.email}</p>
                                            <p className="text-[10px] text-slate-400 italic">{paciente.telefono}</p>
                                        </td>
                                        <td className="px-6 py-4">
                                            <span className={`px-3 py-1 rounded-full text-[10px] font-bold uppercase transition-colors ${paciente.activo ? 'bg-emerald-50 text-emerald-600 border border-emerald-100' : 'bg-red-50 text-red-600 border border-red-100'}`}>
                                                {paciente.activo ? 'Activo' : 'Inactivo'}
                                            </span>
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

            {/* Modal para Nuevo Paciente */}
            {isModalOpen && (
                <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-in fade-in duration-300">
                    <div className="bg-white w-full max-w-2xl rounded-3xl shadow-2xl overflow-hidden border border-slate-100 animate-in zoom-in-95 duration-300">
                        <div className="flex items-center justify-between p-8 border-b border-slate-50 bg-slate-50/50">
                            <div>
                                <h3 className="text-2xl font-bold text-slate-800 italic">Registrar Paciente</h3>
                                <p className="text-sm text-slate-500">Completa la ficha técnica del paciente</p>
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
                                        placeholder="Ej. Juan"
                                    />
                                    {errors.nombre && <p className="text-red-500 text-[10px] font-bold mt-1 uppercase">Obligatorio</p>}
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Apellido</label>
                                    <input
                                        {...register('apellido', { required: true })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white"
                                        placeholder="Ej. Perez"
                                    />
                                    {errors.apellido && <p className="text-red-500 text-[10px] font-bold mt-1 uppercase">Obligatorio</p>}
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Cédula / ID</label>
                                    <input
                                        {...register('cedula', { required: true })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white"
                                        placeholder="0000000000"
                                    />
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Email</label>
                                    <input
                                        {...register('email', { required: true, pattern: /^\S+@\S+$/i })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white"
                                        placeholder="correo@ejemplo.com"
                                    />
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Teléfono</label>
                                    <input
                                        {...register('telefono', { required: true })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white"
                                        placeholder="+593 999..."
                                    />
                                </div>
                                <div className="space-y-1">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Género</label>
                                    <select
                                        {...register('genero', { required: true })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white appearance-none"
                                    >
                                        <option value="MASCULINO">Masculino</option>
                                        <option value="FEMENINO">Femenino</option>
                                        <option value="OTRO">Otro</option>
                                    </select>
                                </div>
                                <div className="space-y-1 md:col-span-2">
                                    <label className="text-[10px] uppercase font-bold text-slate-400 tracking-widest pl-1">Fecha de Nacimiento</label>
                                    <input
                                        type="date"
                                        {...register('fechaNacimiento', { required: true })}
                                        className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:ring-2 focus:ring-medical-500/20 focus:outline-none focus:bg-white"
                                    />
                                </div>
                            </div>

                            <div className="pt-6 flex gap-4">
                                <button
                                    type="button"
                                    onClick={() => setIsModalOpen(false)}
                                    className="flex-1 py-4 text-slate-500 font-bold hover:bg-slate-50 rounded-2xl transition-all"
                                >
                                    Cancelar
                                </button>
                                <button
                                    type="submit"
                                    disabled={saving}
                                    className="flex-1 py-4 bg-medical-500 text-white rounded-2xl font-bold hover:bg-medical-600 transition-all shadow-lg shadow-medical-500/20 disabled:bg-slate-200 disabled:shadow-none flex items-center justify-center"
                                >
                                    {saving ? (
                                        <div className="w-6 h-6 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
                                    ) : (
                                        'Guardar Paciente'
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

export default Pacientes;
