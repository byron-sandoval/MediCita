import api from '../api/axios';
import type { Paciente } from '../types';

export const pacienteService = {
    getAll: async () => {
        const response = await api.get<Paciente[]>('/api/pacientes');
        return response.data;
    },

    getById: async (id: number) => {
        const response = await api.get<Paciente>(`/api/pacientes/${id}`);
        return response.data;
    },

    getByKeycloakId: async (keycloakId: string) => {
        const response = await api.get<Paciente>(`/api/pacientes/keycloak/${keycloakId}`);
        return response.data;
    },

    create: async (paciente: Partial<Paciente>) => {
        const response = await api.post<Paciente>('/api/pacientes', paciente);
        return response.data;
    },

    update: async (id: number, paciente: Partial<Paciente>) => {
        const response = await api.put<Paciente>(`/api/pacientes/${id}`, paciente);
        return response.data;
    },

    delete: async (id: number) => {
        await api.delete(`/api/pacientes/${id}`);
    },
};
