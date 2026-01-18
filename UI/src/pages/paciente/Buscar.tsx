import React, { useEffect, useState } from 'react';
import { medicoService } from '../../services/medicoService';
import { citaService } from '../../services/citaService';
import { pacienteService } from '../../services/pacienteService';
import { useAuth } from '../../context/AuthContext';
import { Especialidad, type Medico, type Paciente } from '../../types';
import { Search, Stethoscope, Star, Clock, Calendar, ChevronRight, Check } from 'lucide-react';
import { toast } from 'sonner';

const PacienteBuscar: React.FC = () => {
    const { user } = useAuth();
    const [medicos, setMedicos] = useState<Medico[]>([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedMedico, setSelectedMedico] = useState<Medico | null>(null);
    const [step, setStep] = useState(1);
    const [motivo, setMotivo] = useState('');
    const [pacienteData, setPacienteData] = useState<Paciente | null>(null);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        const loadData = async () => {
            try {
                setLoading(true);
                // Get medicos (allowed for everyone)
                const availableMedicos = await medicoService.getAll();
                setMedicos(availableMedicos.filter(med => med.activo));

                // Fetch only THIS patient's data by Keycloak ID (New Endpoint)
                if (user?.id) {
                    try {
                        const record = await pacienteService.getByKeycloakId(user.id);
                        setPacienteData(record);
                    } catch (e) {
                        console.log("Paciente no encontrado en DB, se creará al agendar.");
                    }
                }
            } catch (err) {
                console.error("Error during initial sync", err);
            } finally {
                setLoading(false);
            }
        };

        if (user?.id) loadData();
    }, [user?.id]);

    const handleAgendar = async () => {
        if (!selectedMedico) {
            toast.error("Selecciona un médico");
            return;
        }

        setSaving(true);
        let finalPaciente = pacienteData;

        try {
            // Auto-create if not found by Keycloak ID
            if (!finalPaciente && user) {
                toast.loading("Sincronizando perfil médico...", { id: 'booking' });
                const [nombre, ...apellidoParts] = (user.name || 'Usuario').split(' ');
                const newPaciente: Partial<Paciente> = {
                    nombre: nombre,
                    apellido: apellidoParts.join(' ') || 'Paciente',
                    email: user.email,
                    cedula: 'ID-' + user.id.split('-')[0] + '-' + Date.now().toString().slice(-4),
                    telefono: '0000000000',
                    fechaNacimiento: '2000-01-01',
                    genero: 'OTRO' as any,
                    keycloakId: user.id,
                    activo: true
                };
                finalPaciente = await pacienteService.create(newPaciente);
                setPacienteData(finalPaciente);
            }

            if (finalPaciente?.id) {
                await createCita(finalPaciente.id);
            } else {
                throw new Error("No pudimos obtener el perfil de paciente");
            }
        } catch (error: any) {
            console.error("Booking error:", error);
            const msg = error.response?.data?.title || error.message || "Error inesperado";
            toast.error(`Error: ${msg}`, { id: 'booking' });
            setSaving(false);
        }
    };

    const createCita = async (pacienteId: number) => {
        try {
            const nuevaCita = {
                fechaHora: new Date(Date.now() + 86400000).toISOString(),
                motivo,
                estado: 'PENDIENTE' as any,
                medico: { id: selectedMedico?.id } as any,
                paciente: { id: pacienteId } as any,
                activo: true
            };
            await citaService.create(nuevaCita);
            toast.success("¡Cita agendada con éxito!", { id: 'booking' });
            setStep(3);
        } catch (err) {
            toast.error("Error al agendar cita. Revisa tu conexión.", { id: 'booking' });
        } finally {
            setSaving(false);
        }
    };

    const filtered = medicos.filter(m =>
        `${m.nombre} ${m.apellido}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
        m.especialidad.includes(searchTerm)
    );

    if (step === 3) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[60vh] text-center space-y-6 animate-in zoom-in-95 duration-500">
                <div className="w-20 h-20 bg-emerald-500 text-white rounded-full flex items-center justify-center shadow-xl shadow-emerald-500/30">
                    <Check className="w-10 h-10" />
                </div>
                <div>
                    <h2 className="text-3xl font-bold text-slate-800 italic">¡Cita Agendada!</h2>
                    <p className="text-slate-500 mt-2 max-w-sm mx-auto">
                        Tu solicitud ha sido enviada al Dr. {selectedMedico?.nombre}. Recibirás un correo cuando sea confirmada.
                    </p>
                </div>
                <button
                    onClick={() => window.location.href = '/dashboard'}
                    className="px-8 py-3 bg-slate-800 text-white rounded-xl font-bold hover:bg-slate-900 transition-all"
                >
                    Volver al Dashboard
                </button>
            </div>
        );
    }

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div className="bg-white p-4 rounded-2xl shadow-sm border border-slate-100 flex items-center justify-center gap-8 text-[10px] font-bold uppercase tracking-widest">
                <div className={`flex items-center gap-2 ${step === 1 ? 'text-medical-500' : 'text-slate-300'}`}>
                    <span className={`w-6 h-6 rounded-full flex items-center justify-center border-2 ${step === 1 ? 'border-medical-500' : 'border-slate-100'}`}>1</span>
                    Elegir Médico
                </div>
                <span className="w-8 h-0.5 bg-slate-50"></span>
                <div className={`flex items-center gap-2 ${step === 2 ? 'text-medical-500' : 'text-slate-300'}`}>
                    <span className={`w-6 h-6 rounded-full flex items-center justify-center border-2 ${step === 2 ? 'border-medical-500' : 'border-slate-100'}`}>2</span>
                    Detalles
                </div>
            </div>

            {step === 1 ? (
                <>
                    <div>
                        <h2 className="text-3xl font-bold text-slate-800 italic">Agendar Nueva Cita</h2>
                        <p className="text-slate-500 mt-1">Busca al especialista que necesitas.</p>
                    </div>

                    <div className="relative">
                        <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 w-5 h-5" />
                        <input
                            type="text"
                            placeholder="Buscar por nombre o especialidad..."
                            className="w-full pl-12 pr-6 py-4 bg-white border border-slate-100 rounded-2xl shadow-sm focus:ring-2 focus:ring-medical-500/20 outline-none text-slate-600 transition-all"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {loading ? (
                            <div className="col-span-full py-20 flex justify-center"><div className="animate-spin rounded-full h-10 w-10 border-t-2 border-medical-500"></div></div>
                        ) : filtered.map((m) => (
                            <div key={m.id} className="bg-white p-6 rounded-3xl shadow-sm border border-slate-100 hover:shadow-xl hover:-translate-y-1 transition-all group">
                                <div className="flex items-center gap-4 mb-6">
                                    <div className="w-16 h-16 rounded-2xl bg-slate-50 group-hover:bg-medical-50 flex items-center justify-center text-slate-300 group-hover:text-medical-600 transition-colors">
                                        <Stethoscope className="w-8 h-8" />
                                    </div>
                                    <div>
                                        <h4 className="font-bold text-slate-800 uppercase tracking-tight">Dr. {m.nombre} {m.apellido}</h4>
                                        <p className="text-xs text-medical-600 font-bold uppercase">{m.especialidad}</p>
                                        <div className="flex items-center gap-1 mt-1">
                                            <Star className="w-3 h-3 text-yellow-400 fill-yellow-400" />
                                            <span className="text-[10px] font-bold text-slate-400">4.9 (120 Reseñas)</span>
                                        </div>
                                    </div>
                                </div>
                                <div className="flex justify-between items-center pt-6 border-t border-slate-50">
                                    <div className="text-xs italic font-serif">
                                        <p className="text-slate-400 font-bold uppercase tracking-tighter not-italic">Consulta</p>
                                        <p className="text-lg font-bold text-emerald-600">${m.tarifaConsulta}</p>
                                    </div>
                                    <button
                                        onClick={() => { setSelectedMedico(m); setStep(2); }}
                                        className="px-6 py-2 bg-slate-800 text-white rounded-xl text-xs font-bold hover:bg-slate-900 transition-all flex items-center group/btn"
                                    >
                                        Elegir
                                        <ChevronRight className="w-4 h-4 ml-2 group-hover/btn:translate-x-1 transition-transform" />
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                </>
            ) : (
                <div className="bg-white rounded-3xl p-10 max-w-2xl mx-auto shadow-sm border border-slate-100 animate-in slide-in-from-right-10 duration-500">
                    <h3 className="text-2xl font-bold text-slate-800 mb-8 italic">Detalles de la Cita</h3>
                    <div className="space-y-6">
                        <div className="flex items-center gap-4 p-4 bg-slate-50 rounded-2xl border border-slate-100">
                            <div className="w-12 h-12 bg-white rounded-xl flex items-center justify-center shadow-sm">
                                <Stethoscope className="w-6 h-6 text-medical-600" />
                            </div>
                            <div>
                                <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Especialista</p>
                                <p className="font-bold text-slate-800">Dr. {selectedMedico?.nombre} {selectedMedico?.apellido}</p>
                            </div>
                        </div>

                        <div className="space-y-2">
                            <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest pl-1">Motivo de la Consulta</label>
                            <textarea
                                className="w-full px-6 py-4 bg-slate-50 border border-slate-100 rounded-2xl focus:ring-2 focus:ring-medical-500/20 outline-none transition-all min-h-[120px]"
                                placeholder="Describe brevemente tus síntomas..."
                                value={motivo}
                                onChange={e => setMotivo(e.target.value)}
                            />
                        </div>

                        <div className="flex gap-4 pt-4">
                            <button
                                onClick={() => setStep(1)}
                                className="flex-1 py-4 text-slate-500 font-bold hover:bg-slate-50 rounded-2xl transition-all"
                                disabled={saving}
                            >
                                Atrás
                            </button>
                            <button
                                onClick={handleAgendar}
                                disabled={!motivo || saving}
                                className="flex-2 py-4 bg-medical-500 text-white rounded-2xl font-bold shadow-lg shadow-medical-500/20 hover:bg-medical-600 transition-all disabled:bg-slate-200 disabled:shadow-none flex items-center justify-center min-w-[200px]"
                            >
                                {saving ? (
                                    <div className="w-6 h-6 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
                                ) : (
                                    'Confirmar y Agendar'
                                )}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default PacienteBuscar;
