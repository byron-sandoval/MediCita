import React, { useEffect, useState } from 'react';
import { historiaClinicaService } from '../../services/historiaClinicaService';
import { useAuth } from '../../context/AuthContext';
import type { HistoriaClinica } from '../../types';
import { FileText, Calendar, Download, AlertCircle } from 'lucide-react';

const PacienteHistorial: React.FC = () => {
    const { user } = useAuth();
    const [historias, setHistorias] = useState<HistoriaClinica[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchMiHistorial = async () => {
            try {
                setLoading(true);
                const data = await historiaClinicaService.getAll();
                const filter = data.filter(h => h.paciente?.keycloakId === user?.id);
                setHistorias(filter);
            } catch (error) {
                console.error('Error fetching patient history:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchMiHistorial();
    }, [user?.id]);

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div>
                <h2 className="text-3xl font-bold text-slate-800 italic">Mi Historial Clínico</h2>
                <p className="text-slate-500 mt-1">Consulta tus diagnósticos y tratamientos pasados.</p>
            </div>

            <div className="space-y-4">
                {loading ? (
                    <div className="flex justify-center p-20"><div className="animate-spin rounded-full h-10 w-10 border-t-2 border-medical-500"></div></div>
                ) : historias.length === 0 ? (
                    <div className="p-20 text-center bg-white rounded-3xl border border-dashed border-slate-200">
                        <FileText className="w-12 h-12 text-slate-100 mx-auto mb-4" />
                        <p className="text-slate-400">Aún no tienes registros en tu historia clínica</p>
                    </div>
                ) : historias.map((h) => (
                    <div key={h.id} className="bg-white p-8 rounded-3xl shadow-sm border border-slate-100 hover:border-medical-200 transition-all group relative overflow-hidden">
                        <div className="absolute top-0 right-0 w-32 h-32 bg-medical-50/30 rounded-full -mr-16 -mt-16 group-hover:bg-medical-50 transition-colors"></div>

                        <div className="relative z-10">
                            <div className="flex items-center gap-3 mb-6">
                                <div className="p-3 bg-medical-500 text-white rounded-2xl shadow-lg shadow-medical-500/20">
                                    <Calendar className="w-5 h-5" />
                                </div>
                                <div>
                                    <p className="text-xs font-bold text-slate-400 uppercase tracking-widest">Fecha de Consulta</p>
                                    <p className="font-bold text-slate-700">{new Date(h.fechaCreacion || '').toLocaleDateString()}</p>
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
                                <div className="space-y-2">
                                    <h5 className="flex items-center text-xs font-bold text-medical-600 uppercase tracking-tighter">
                                        <AlertCircle className="w-3 h-3 mr-1" />
                                        Diagnóstico
                                    </h5>
                                    <p className="text-slate-600 leading-relaxed text-sm bg-slate-50 p-4 rounded-2xl border border-slate-100">
                                        {h.diagnostico}
                                    </p>
                                </div>
                                <div className="space-y-2">
                                    <h5 className="flex items-center text-xs font-bold text-emerald-600 uppercase tracking-tighter">
                                        <FileText className="w-3 h-3 mr-1" />
                                        Tratamiento
                                    </h5>
                                    <p className="text-slate-600 leading-relaxed text-sm bg-emerald-50/30 p-4 rounded-2xl border border-emerald-100">
                                        {h.tratamiento}
                                    </p>
                                </div>
                            </div>

                            <button className="flex items-center gap-2 text-[10px] font-bold text-slate-400 hover:text-medical-600 transition-colors uppercase tracking-widest">
                                <Download className="w-4 h-4" />
                                Descargar Resumen PDF
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default PacienteHistorial;
