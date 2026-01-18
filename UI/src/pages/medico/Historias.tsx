import React, { useEffect, useState } from 'react';
import { historiaClinicaService } from '../../services/historiaClinicaService';
import { pacienteService } from '../../services/pacienteService';
import { useAuth } from '../../context/AuthContext';
import type { HistoriaClinica, Paciente } from '../../types';
import { FileText, Plus, Search, Calendar, ChevronRight } from 'lucide-react';
import { toast } from 'sonner';

const MedicoHistorias: React.FC = () => {
    const { user } = useAuth();
    const [historias, setHistorias] = useState<HistoriaClinica[]>([]);
    const [pacientes, setPacientes] = useState<Paciente[]>([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newHistory, setNewHistory] = useState<Partial<HistoriaClinica>>({
        diagnostico: '',
        tratamiento: '',
        activo: true
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [hData, pData] = await Promise.all([
                    historiaClinicaService.getAll(),
                    pacienteService.getAll()
                ]);
                setHistorias(hData);
                setPacientes(pData);
            } catch (err) {
                console.error("Error fetching medical histories", err);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    const handleCreate = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await historiaClinicaService.create({
                ...newHistory,
                fechaCreacion: new Date().toISOString()
            });
            toast.success('Registro añadido a la historia clínica');
            setIsModalOpen(false);
            const updated = await historiaClinicaService.getAll();
            setHistorias(updated);
        } catch (err) {
            toast.error('Error al guardar el registro');
        }
    };

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div className="flex justify-between items-center">
                <div>
                    <h2 className="text-3xl font-bold text-slate-800 italic">Historias Clínicas</h2>
                    <p className="text-slate-500 mt-1">Registros médicos y diagnósticos de pacientes.</p>
                </div>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="flex items-center px-6 py-3 bg-medical-500 text-white rounded-xl hover:bg-medical-600 shadow-lg shadow-medical-500/20 font-bold transition-all active:scale-95"
                >
                    <Plus className="w-5 h-5 mr-2" />
                    Nuevo Registro
                </button>
            </div>

            <div className="bg-white rounded-3xl border border-slate-100 shadow-sm overflow-hidden">
                {loading ? (
                    <div className="p-20 flex justify-center"><div className="animate-spin rounded-full h-10 w-10 border-t-2 border-medical-500"></div></div>
                ) : historias.length === 0 ? (
                    <div className="p-20 text-center">
                        <FileText className="w-12 h-12 text-slate-100 mx-auto mb-4" />
                        <p className="text-slate-400">No hay historias clínicas registradas</p>
                    </div>
                ) : (
                    <div className="divide-y divide-slate-50">
                        {historias.map((h) => (
                            <div key={h.id} className="p-6 hover:bg-slate-50/50 transition-colors group cursor-pointer">
                                <div className="flex items-center justify-between mb-4">
                                    <div className="flex items-center gap-3">
                                        <div className="p-2 bg-blue-50 rounded-lg text-blue-600">
                                            <Calendar className="w-4 h-4" />
                                        </div>
                                        <span className="text-xs font-bold text-slate-400 uppercase tracking-widest">
                                            {new Date(h.fechaCreacion || '').toLocaleDateString()}
                                        </span>
                                    </div>
                                    <ChevronRight className="w-5 h-5 text-slate-300 group-hover:text-medical-500 group-hover:translate-x-1 transition-all" />
                                </div>
                                <h4 className="font-bold text-slate-800 text-lg mb-2">
                                    {h.paciente?.nombre} {h.paciente?.apellido}
                                </h4>
                                <div className="space-y-1">
                                    <p className="text-sm text-slate-600 line-clamp-2">
                                        <span className="font-bold text-slate-400 text-[10px] uppercase mr-2 tracking-tighter">Diagnóstico:</span>
                                        {h.diagnostico}
                                    </p>
                                    {h.tratamiento && (
                                        <p className="text-sm text-slate-500 italic line-clamp-1">
                                            <span className="font-bold text-slate-400 text-[10px] uppercase mr-2 tracking-tighter">Tratamiento:</span>
                                            {h.tratamiento}
                                        </p>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {isModalOpen && (
                <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm animate-in fade-in">
                    <div className="bg-white w-full max-w-2xl rounded-3xl shadow-2xl overflow-hidden animate-in zoom-in-95">
                        <div className="p-8 border-b border-slate-50 flex justify-between items-center bg-slate-50/50">
                            <h3 className="text-xl font-bold text-slate-800">Nuevo Registro Médico</h3>
                            <button onClick={() => setIsModalOpen(false)} className="text-slate-400 hover:text-slate-600">×</button>
                        </div>
                        <form onSubmit={handleCreate} className="p-8 space-y-4">
                            <div className="space-y-1">
                                <label className="text-[10px] font-bold uppercase text-slate-400 pl-1">Paciente</label>
                                <select
                                    className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:outline-none"
                                    onChange={e => setNewHistory({ ...newHistory, paciente: { id: Number(e.target.value) } as any })}
                                >
                                    <option value="">Selecciona un paciente...</option>
                                    {pacientes.map(p => <option key={p.id} value={p.id}>{p.nombre} {p.apellido}</option>)}
                                </select>
                            </div>
                            <div className="space-y-1">
                                <label className="text-[10px] font-bold uppercase text-slate-400 pl-1">Diagnóstico Detallado</label>
                                <textarea
                                    required
                                    className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl min-h-[120px]"
                                    value={newHistory.diagnostico}
                                    onChange={e => setNewHistory({ ...newHistory, diagnostico: e.target.value })}
                                />
                            </div>
                            <div className="space-y-1">
                                <label className="text-[10px] font-bold uppercase text-slate-400 pl-1">Tratamiento Recomendado</label>
                                <textarea
                                    className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl min-h-[80px]"
                                    value={newHistory.tratamiento}
                                    onChange={e => setNewHistory({ ...newHistory, tratamiento: e.target.value })}
                                />
                            </div>
                            <button className="w-full py-4 bg-medical-500 text-white rounded-2xl font-bold shadow-lg shadow-medical-500/20 hover:bg-medical-600 transition-all">
                                Guardar Expediente
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default MedicoHistorias;
