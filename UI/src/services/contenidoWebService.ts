import api from '../api/axios';

export interface ContenidoWeb {
    id?: number;
    clave: string;
    valorTexto?: string;
    imagen?: string;
    imagenContentType?: string;
    activo: boolean;
}

export const contenidoWebService = {
    getAll: async () => {
        const response = await api.get<ContenidoWeb[]>('/api/contenido-webs');
        return response.data;
    },

    getById: async (id: number) => {
        const response = await api.get<ContenidoWeb>(`/api/contenido-webs/${id}`);
        return response.data;
    },

    create: async (contenido: Partial<ContenidoWeb>) => {
        const response = await api.post<ContenidoWeb>('/api/contenido-webs', contenido);
        return response.data;
    },

    update: async (id: number, contenido: Partial<ContenidoWeb>) => {
        const response = await api.put<ContenidoWeb>(`/api/contenido-webs/${id}`, contenido);
        return response.data;
    },

    delete: async (id: number) => {
        await api.delete(`/api/contenido-webs/${id}`);
    },
};
