export type Role = 'ROLE_ADMIN' | 'ROLE_MEDICO' | 'ROLE_USER';

export enum Especialidad {
    CARDIOLOGIA = 'CARDIOLOGIA',
    DERMATOLOGIA = 'DERMATOLOGIA',
    GINECOLOGIA = 'GINECOLOGIA',
    PEDIATRIA = 'PEDIATRIA',
    NEUROLOGIA = 'NEUROLOGIA',
    ODONTOLOGIA = 'ODONTOLOGIA',
    PSIQUIATRIA = 'PSIQUIATRIA',
}

export enum EstadoCita {
    PENDIENTE = 'PENDIENTE',
    CONFIRMADA = 'CONFIRMADA',
    COMPLETADA = 'COMPLETADA',
    CANCELADA = 'CANCELADA',
    AUSENTE = 'AUSENTE',
}

export enum Genero {
    MASCULINO = 'MASCULINO',
    FEMENINO = 'FEMENINO',
    OTRO = 'OTRO',
}

export interface Adjunto {
    id?: number;
    nombre: string;
    contenido?: string;
    contenidoContentType?: string;
    tipoContenido?: string;
    fechaSubida?: string;
}

export interface HistoriaClinica {
    id?: number;
    fechaCreacion?: string;
    diagnostico: string;
    tratamiento: string;
    activo: boolean;
    paciente?: Paciente;
}

export interface Paciente {
    id?: number;
    nombre: string;
    apellido: string;
    cedula: string;
    email: string;
    telefono: string;
    fechaNacimiento: string;
    genero: Genero;
    keycloakId: string;
    activo: boolean;
    foto?: Adjunto;
}

export interface Medico {
    id?: number;
    nombre: string;
    apellido: string;
    email: string;
    numeroLicencia: string;
    especialidad: Especialidad;
    tarifaConsulta: number;
    keycloakId: string;
    activo: boolean;
    foto?: Adjunto;
}

export interface Cita {
    id?: number;
    fechaHora: string;
    motivo: string;
    estado: EstadoCita;
    enlaceTelemedicina?: string;
    costo?: number;
    pagado?: boolean;
    activo: boolean;
    medico?: Medico;
    paciente?: Paciente;
}

export interface Disponibilidad {
    id?: number;
    dia: string;
    horaInicio: string;
    horaFin: string;
    esDescanso: boolean;
    medico?: Medico;
}

export enum DiaSemana {
    LUNES = 'LUNES',
    MARTES = 'MARTES',
    MIERCOLES = 'MIERCOLES',
    JUEVES = 'JUEVES',
    VIERNES = 'VIERNES',
    SABADO = 'SABADO',
    DOMINGO = 'DOMINGO',
}

export interface ContenidoWeb {
    id?: number;
    clave: string;
    valorTexto?: string;
    imagen?: string;
    imagenContentType?: string;
    activo: boolean;
}
