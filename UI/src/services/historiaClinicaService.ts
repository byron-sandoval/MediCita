import api from '../api/axios';
import type { HistoriaClinica } from '../types';

export const historiaClinicaService = {
    getAll: async () => {
        const response = await api.get<HistoriaClinica[]>('/api/historia-clinicas');
        return response.data;
    },

    getById: async (id: number) => {
        const response = await api.get<HistoriaClinica>(`/api/historia-clinicas/${id}`);
        return response.data;
    },

    create: async (historia: Partial<HistoriaClinica>) => {
        const response = await api.post<HistoriaClinica>('/api/historia-clinicas', historia);
        return response.data;
    },

    update: async (id: number, historia: Partial<HistoriaClinica>) => {
        const response = await api.put<HistoriaClinica>(`/api/historia-clinicas/${id}`, historia);
        return response.data;
    },

    delete: async (id: number) => {
        await api.delete(`/api/historia-clinicas/${id}`);
    },
};
