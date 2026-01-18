import React, { useEffect, useState } from 'react';
import { contenidoWebService, type ContenidoWeb } from '../../services/contenidoWebService';
import { Settings, Plus, Trash2, Edit, Save, X, Globe, Image as ImageIcon } from 'lucide-react';
import { toast } from 'sonner';

const AdminConfiguracion: React.FC = () => {
    const [contenidos, setContenidos] = useState<ContenidoWeb[]>([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingItem, setEditingItem] = useState<Partial<ContenidoWeb> | null>(null);

    useEffect(() => {
        fetchContenidos();
    }, []);

    const fetchContenidos = async () => {
        try {
            setLoading(true);
            const data = await contenidoWebService.getAll();
            setContenidos(data);
        } catch (error) {
            console.error('Error fetching web content:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSave = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!editingItem?.clave) return;

        try {
            if (editingItem.id) {
                await contenidoWebService.update(editingItem.id, editingItem);
                toast.success('Contenido actualizado');
            } else {
                await contenidoWebService.create({ ...editingItem, activo: true });
                toast.success('Contenido creado');
            }
            setIsModalOpen(false);
            setEditingItem(null);
            fetchContenidos();
        } catch (error) {
            toast.error('Error al guardar');
        }
    };

    return (
        <div className="space-y-6 animate-in fade-in duration-500">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div>
                    <h2 className="text-3xl font-bold text-slate-800 italic">Configuración Web</h2>
                    <p className="text-slate-500 mt-1">Administra los textos e imágenes dinámicas del portal.</p>
                </div>
                <button
                    onClick={() => { setEditingItem({}); setIsModalOpen(true); }}
                    className="flex items-center px-6 py-3 bg-medical-500 text-white rounded-xl hover:bg-medical-600 transition-all shadow-lg shadow-medical-500/20 font-bold active:scale-95"
                >
                    <Plus className="w-5 h-5 mr-2" />
                    Nuevo Parámetro
                </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {loading ? (
                    <div className="col-span-full p-20 flex justify-center">
                        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-medical-500"></div>
                    </div>
                ) : contenidos.length === 0 ? (
                    <div className="col-span-full p-20 text-center bg-white rounded-2xl border border-dashed border-slate-200">
                        <Globe className="w-12 h-12 text-slate-200 mx-auto mb-4" />
                        <p className="text-slate-400 font-medium">No hay contenido dinámico configurado</p>
                    </div>
                ) : contenidos.map((item) => (
                    <div key={item.id} className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 hover:shadow-md transition-all group">
                        <div className="flex justify-between items-start mb-4">
                            <div className="p-2 bg-slate-50 rounded-lg group-hover:bg-medical-50 transition-colors">
                                <Settings className="w-5 h-5 text-slate-400 group-hover:text-medical-600" />
                            </div>
                            <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                                <button
                                    onClick={() => { setEditingItem(item); setIsModalOpen(true); }}
                                    className="p-2 text-slate-400 hover:text-medical-600 hover:bg-medical-50 rounded-lg"
                                >
                                    <Edit className="w-4 h-4" />
                                </button>
                                <button
                                    onClick={async () => {
                                        if (item.id) {
                                            await contenidoWebService.delete(item.id);
                                            toast.success('Eliminado');
                                            fetchContenidos();
                                        }
                                    }}
                                    className="p-2 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg"
                                >
                                    <Trash2 className="w-4 h-4" />
                                </button>
                            </div>
                        </div>
                        <h4 className="font-bold text-slate-800 text-sm mb-1 uppercase tracking-tighter">{item.clave}</h4>
                        <p className="text-slate-500 text-xs line-clamp-3 mb-4">{item.valorTexto || 'Sin valor de texto'}</p>
                        {item.imagen && (
                            <div className="flex items-center text-[10px] font-bold text-medical-600 bg-medical-50 px-2 py-1 rounded w-fit capitalize">
                                <ImageIcon className="w-3 h-3 mr-1" />
                                Imagen Adjunta
                            </div>
                        )}
                    </div>
                ))}
            </div>

            {isModalOpen && (
                <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm">
                    <div className="bg-white w-full max-w-lg rounded-3xl shadow-2xl overflow-hidden animate-in zoom-in-95">
                        <div className="p-8 border-b border-slate-50 flex justify-between items-center">
                            <h3 className="text-xl font-bold text-slate-800">Editar Parámetro</h3>
                            <button onClick={() => setIsModalOpen(false)}><X className="w-6 h-6 text-slate-300" /></button>
                        </div>
                        <form onSubmit={handleSave} className="p-8 space-y-4">
                            <div className="space-y-1">
                                <label className="text-[10px] font-bold uppercase text-slate-400 pl-1">Clave Única</label>
                                <input
                                    required
                                    className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:outline-none focus:ring-2 focus:ring-medical-500/20"
                                    value={editingItem?.clave || ''}
                                    onChange={e => setEditingItem({ ...editingItem, clave: e.target.value })}
                                    placeholder="NOMBRE_APP"
                                />
                            </div>
                            <div className="space-y-1">
                                <label className="text-[10px] font-bold uppercase text-slate-400 pl-1">Valor de Texto</label>
                                <textarea
                                    className="w-full px-4 py-3 bg-slate-50 border border-slate-100 rounded-xl focus:outline-none focus:ring-2 focus:ring-medical-500/20 min-h-[100px]"
                                    value={editingItem?.valorTexto || ''}
                                    onChange={e => setEditingItem({ ...editingItem, valorTexto: e.target.value })}
                                    placeholder="Contenido a mostrar..."
                                />
                            </div>
                            <button
                                type="submit"
                                className="w-full py-4 bg-medical-500 text-white rounded-2xl font-bold flex items-center justify-center gap-2"
                            >
                                <Save className="w-5 h-5" />
                                Guardar Configuración
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminConfiguracion;
