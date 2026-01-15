package com.medicita.app.service.dto;

import com.medicita.app.domain.enumeration.EstadoCita;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.medicita.app.domain.Cita} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CitaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant fechaHora;

    @NotNull
    @Size(max = 500)
    private String motivo;

    @NotNull
    private EstadoCita estado;

    private String enlaceTelemedicina;

    private BigDecimal costo;

    @NotNull
    private Boolean activo;

    private MedicoDTO medico;

    private PacienteDTO paciente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Instant fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getEnlaceTelemedicina() {
        return enlaceTelemedicina;
    }

    public void setEnlaceTelemedicina(String enlaceTelemedicina) {
        this.enlaceTelemedicina = enlaceTelemedicina;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public MedicoDTO getMedico() {
        return medico;
    }

    public void setMedico(MedicoDTO medico) {
        this.medico = medico;
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
        if (!(o instanceof CitaDTO)) {
            return false;
        }

        CitaDTO citaDTO = (CitaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, citaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitaDTO{" +
            "id=" + getId() +
            ", fechaHora='" + getFechaHora() + "'" +
            ", motivo='" + getMotivo() + "'" +
            ", estado='" + getEstado() + "'" +
            ", enlaceTelemedicina='" + getEnlaceTelemedicina() + "'" +
            ", costo=" + getCosto() +
            ", activo='" + getActivo() + "'" +
            ", medico=" + getMedico() +
            ", paciente=" + getPaciente() +
            "}";
    }
}
