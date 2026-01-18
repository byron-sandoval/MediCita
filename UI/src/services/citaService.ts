import api from '../api/axios';
import type { Cita } from '../types';

export const citaService = {
    getAll: async () => {
        const response = await api.get<Cita[]>('/api/citas');
        return response.data;
    },

    getById: async (id: number) => {
        const response = await api.get<Cita>(`/api/citas/${id}`);
        return response.data;
    },

    create: async (cita: Partial<Cita>) => {
        const response = await api.post<Cita>('/api/citas', cita);
        return response.data;
    },

    update: async (id: number, cita: Partial<Cita>) => {
        const response = await api.put<Cita>(`/api/citas/${id}`, cita);
        return response.data;
    },

    delete: async (id: number) => {
        await api.delete(`/api/citas/${id}`);
    },

    // Specific for patients
    getMisCitas: async () => {
        const response = await api.get<Cita[]>('/api/citas/mis-citas');
        return response.data;
    }
};
