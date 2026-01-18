package com.medicita.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.medicita.app.domain.Adjunto} entity.
 */
@Schema(description = "Entidad para subir imágenes (perfil, recetas, estudios)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdjuntoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @Lob
    private byte[] contenido;

    private String contenidoContentType;

    private String tipoContenido;

    private Instant fechaSubida;

    private CitaDTO cita;

    private HistoriaClinicaDTO historiaClinica;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public String getContenidoContentType() {
        return contenidoContentType;
    }

    public void setContenidoContentType(String contenidoContentType) {
        this.contenidoContentType = contenidoContentType;
    }

    public String getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public Instant getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(Instant fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public CitaDTO getCita() {
        return cita;
    }

    public void setCita(CitaDTO cita) {
        this.cita = cita;
    }

    public HistoriaClinicaDTO getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinicaDTO historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdjuntoDTO)) {
            return false;
        }

        AdjuntoDTO adjuntoDTO = (AdjuntoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adjuntoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdjuntoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", contenido='" + getContenido() + "'" +
            ", tipoContenido='" + getTipoContenido() + "'" +
            ", fechaSubida='" + getFechaSubida() + "'" +
            ", cita=" + getCita() +
            ", historiaClinica=" + getHistoriaClinica() +
            "}";
    }
}
