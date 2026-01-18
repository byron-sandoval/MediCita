import api from '../api/axios';
import type { Medico } from '../types';

export const medicoService = {
    getAll: async () => {
        const response = await api.get<Medico[]>('/api/medicos');
        return response.data;
    },

    getById: async (id: number) => {
        const response = await api.get<Medico>(`/api/medicos/${id}`);
        return response.data;
    },

    create: async (medico: Partial<Medico>) => {
        const response = await api.post<Medico>('/api/medicos', medico);
        return response.data;
    },

    update: async (id: number, medico: Partial<Medico>) => {
        const response = await api.put<Medico>(`/api/medicos/${id}`, medico);
        return response.data;
    },

    delete: async (id: number) => {
        await api.delete(`/api/medicos/${id}`);
    },
};
