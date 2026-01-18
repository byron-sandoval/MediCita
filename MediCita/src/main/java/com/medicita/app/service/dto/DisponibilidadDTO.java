package com.medicita.app.service.dto;

import com.medicita.app.domain.enumeration.DiaSemana;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.medicita.app.domain.Disponibilidad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DisponibilidadDTO implements Serializable {

    private Long id;

    @NotNull
    private DiaSemana dia;

    @NotNull
    private LocalTime horaInicio;

    @NotNull
    private LocalTime horaFin;

    private Boolean esDescanso;

    private MedicoDTO medico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Boolean getEsDescanso() {
        return esDescanso;
    }

    public void setEsDescanso(Boolean esDescanso) {
        this.esDescanso = esDescanso;
    }

    public MedicoDTO getMedico() {
        return medico;
    }

    public void setMedico(MedicoDTO medico) {
        this.medico = medico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisponibilidadDTO)) {
            return false;
        }

        DisponibilidadDTO disponibilidadDTO = (DisponibilidadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, disponibilidadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisponibilidadDTO{" +
            "id=" + getId() +
            ", dia='" + getDia() + "'" +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", horaFin='" + getHoraFin() + "'" +
            ", esDescanso='" + getEsDescanso() + "'" +
            ", medico=" + getMedico() +
            "}";
    }
}
