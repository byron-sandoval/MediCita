package com.medicita.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.medicita.app.domain.HistoriaClinica} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriaClinicaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant fechaCreacion;

    @NotNull
    @Size(max = 5000)
    private String diagnostico;

    @Size(max = 5000)
    private String tratamiento;

    @NotNull
    private Boolean activo;

    private PacienteDTO paciente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriaClinicaDTO)) {
            return false;
        }

        HistoriaClinicaDTO historiaClinicaDTO = (HistoriaClinicaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiaClinicaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriaClinicaDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", diagnostico='" + getDiagnostico() + "'" +
            ", tratamiento='" + getTratamiento() + "'" +
            ", activo='" + getActivo() + "'" +
            ", paciente=" + getPaciente() +
            "}";
    }
}
