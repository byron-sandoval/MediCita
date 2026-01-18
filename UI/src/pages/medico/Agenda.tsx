import React, { useEffect, useState } from 'react';
import { citaService } from '../../services/citaService';
import { useAuth } from '../../context/AuthContext';
import type { Cita } from '../../types';
import { Calendar, Clock, MapPin, CheckCircle2, XCircle, AlertCircle } from 'lucide-react';
import { toast } from 'sonner';

const MedicoAgenda: React.FC = () => {
    const { user } = useAuth();
    const [citas, setCitas] = useState<Cita[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchAgenda();
    }, [user?.email]);

    const fetchAgenda = async () => {
        try {
            setLoading(true);
            const data = await citaService.getAll();
            const filterCitas = data.filter(c => c.medico?.email === user?.email);
            setCitas(filterCitas);
        } catch (error) {
            console.error('Error fetching agenda:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleStatusUpdate = async (id: number, nuevoEstado: string) => {
        try {
            await citaService.update(id, { estado: nuevoEstado as any });
            toast.success(`Cita marcada como ${nuevoEstado}`);
            fetchAgenda();
        } catch (error) {
            toast.error('Error al actualizar estado');
        }
    };

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div>
                <h2 className="text-3xl font-bold text-slate-800 italic">Mi Agenda Médica</h2>
                <p className="text-slate-500 mt-1">Gerecia tus consultas programadas para hoy y el futuro.</p>
            </div>

            <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
                {loading ? (
                    <div className="p-20 flex justify-center"><div className="animate-spin rounded-full h-12 w-12 border-t-2 border-medical-500"></div></div>
                ) : citas.length === 0 ? (
                    <div className="p-20 text-center">
                        <Calendar className="w-12 h-12 text-slate-200 mx-auto mb-4" />
                        <p className="text-slate-400 font-medium">No hay citas en tu agenda</p>
                    </div>
                ) : (
                    <div className="divide-y divide-slate-50">
                        {citas.map((cita) => (
                            <div key={cita.id} className="p-6 flex flex-col md:flex-row md:items-center justify-between gap-6 hover:bg-slate-50/50 transition-colors">
                                <div className="flex items-start gap-4">
                                    <div className="p-3 bg-medical-50 rounded-xl">
                                        <Clock className="w-6 h-6 text-medical-600" />
                                    </div>
                                    <div>
                                        <div className="flex items-center gap-2 mb-1">
                                            <p className="font-bold text-slate-800 text-lg uppercase tracking-tight">
                                                {new Date(cita.fechaHora).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                            </p>
                                            <span className={`px-2 py-0.5 rounded text-[10px] font-bold uppercase ${cita.estado === 'PENDIENTE' ? 'bg-orange-50 text-orange-600' : 'bg-emerald-50 text-emerald-600'}`}>
                                                {cita.estado}
                                            </span>
                                        </div>
                                        <p className="font-bold text-slate-700">{cita.paciente?.nombre} {cita.paciente?.apellido}</p>
                                        <p className="text-sm text-slate-400 flex items-center mt-1">
                                            <AlertCircle className="w-3 h-3 mr-1" />
                                            Motivo: {cita.motivo}
                                        </p>
                                    </div>
                                </div>

                                <div className="flex items-center gap-3">
                                    {cita.estado === 'PENDIENTE' && (
                                        <>
                                            <button
                                                onClick={() => handleStatusUpdate(cita.id!, 'COMPLETADA')}
                                                className="flex-1 md:flex-none flex items-center justify-center px-4 py-2 bg-emerald-500 text-white rounded-lg font-bold text-sm hover:bg-emerald-600 transition-all shadow-md shadow-emerald-500/20"
                                            >
                                                <CheckCircle2 className="w-4 h-4 mr-2" />
                                                Completar
                                            </button>
                                            <button
                                                onClick={() => handleStatusUpdate(cita.id!, 'CANCELADA')}
                                                className="flex-1 md:flex-none flex items-center justify-center px-4 py-2 bg-white text-red-500 border border-red-200 rounded-lg font-bold text-sm hover:bg-red-50 transition-all"
                                            >
                                                <XCircle className="w-4 h-4 mr-2" />
                                                Cancelar
                                            </button>
                                        </>
                                    )}
                                    <button className="p-2 text-slate-400 hover:text-medical-600 hover:bg-medical-50 rounded-lg transition-all">
                                        <MapPin className="w-5 h-5" />
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default MedicoAgenda;
