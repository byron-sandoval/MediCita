import api from '../api/axios';
import type { Disponibilidad } from '../types';

export const disponibilidadService = {
    getAll: async () => {
        const response = await api.get<Disponibilidad[]>('/api/disponibilidads');
        return response.data;
    },

    getById: async (id: number) => {
        const response = await api.get<Disponibilidad>(`/api/disponibilidads/${id}`);
        return response.data;
    },

    create: async (disponibilidad: Partial<Disponibilidad>) => {
        const response = await api.post<Disponibilidad>('/api/disponibilidads', disponibilidad);
        return response.data;
    },

    update: async (id: number, disponibilidad: Partial<Disponibilidad>) => {
        const response = await api.put<Disponibilidad>(`/api/disponibilidads/${id}`, disponibilidad);
        return response.data;
    },

    delete: async (id: number) => {
        await api.delete(`/api/disponibilidads/${id}`);
    },
};
