package com.medicita.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medicita.app.domain.enumeration.DiaSemana;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * A Disponibilidad.
 */
@Entity
@Table(name = "disponibilidad")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Disponibilidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dia", nullable = false)
    private DiaSemana dia;

    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @NotNull
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "es_descanso")
    private Boolean esDescanso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "foto" }, allowSetters = true)
    private Medico medico;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Disponibilidad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiaSemana getDia() {
        return this.dia;
    }

    public Disponibilidad dia(DiaSemana dia) {
        this.setDia(dia);
        return this;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

    public LocalTime getHoraInicio() {
        return this.horaInicio;
    }

    public Disponibilidad horaInicio(LocalTime horaInicio) {
        this.setHoraInicio(horaInicio);
        return this;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return this.horaFin;
    }

    public Disponibilidad horaFin(LocalTime horaFin) {
        this.setHoraFin(horaFin);
        return this;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Boolean getEsDescanso() {
        return this.esDescanso;
    }

    public Disponibilidad esDescanso(Boolean esDescanso) {
        this.setEsDescanso(esDescanso);
        return this;
    }

    public void setEsDescanso(Boolean esDescanso) {
        this.esDescanso = esDescanso;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Disponibilidad medico(Medico medico) {
        this.setMedico(medico);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Disponibilidad)) {
            return false;
        }
        return getId() != null && getId().equals(((Disponibilidad) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Disponibilidad{" +
            "id=" + getId() +
            ", dia='" + getDia() + "'" +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", horaFin='" + getHoraFin() + "'" +
            ", esDescanso='" + getEsDescanso() + "'" +
            "}";
    }
}
