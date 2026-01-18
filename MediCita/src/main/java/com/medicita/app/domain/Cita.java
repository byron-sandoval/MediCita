package com.medicita.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medicita.app.domain.enumeration.EstadoCita;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * A Cita.
 */
@Entity
@Table(name = "cita")
@SQLDelete(sql = "UPDATE cita SET activo = false WHERE id = ?")
@Where(clause = "activo = true")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_hora", nullable = false)
    private Instant fechaHora;

    @NotNull
    @Size(max = 500)
    @Column(name = "motivo", length = 500, nullable = false)
    private String motivo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCita estado;

    @Column(name = "enlace_telemedicina")
    private String enlaceTelemedicina;

    @Column(name = "costo", precision = 21, scale = 2)
    private BigDecimal costo;

    @Column(name = "pagado")
    private Boolean pagado;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "foto" }, allowSetters = true)
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "foto" }, allowSetters = true)
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cita id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaHora() {
        return this.fechaHora;
    }

    public Cita fechaHora(Instant fechaHora) {
        this.setFechaHora(fechaHora);
        return this;
    }

    public void setFechaHora(Instant fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public Cita motivo(String motivo) {
        this.setMotivo(motivo);
        return this;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EstadoCita getEstado() {
        return this.estado;
    }

    public Cita estado(EstadoCita estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getEnlaceTelemedicina() {
        return this.enlaceTelemedicina;
    }

    public Cita enlaceTelemedicina(String enlaceTelemedicina) {
        this.setEnlaceTelemedicina(enlaceTelemedicina);
        return this;
    }

    public void setEnlaceTelemedicina(String enlaceTelemedicina) {
        this.enlaceTelemedicina = enlaceTelemedicina;
    }

    public BigDecimal getCosto() {
        return this.costo;
    }

    public Cita costo(BigDecimal costo) {
        this.setCosto(costo);
        return this;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Boolean getPagado() {
        return this.pagado;
    }

    public Cita pagado(Boolean pagado) {
        this.setPagado(pagado);
        return this;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Cita activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Cita medico(Medico medico) {
        this.setMedico(medico);
        return this;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Cita paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cita)) {
            return false;
        }
        return getId() != null && getId().equals(((Cita) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cita{" +
                "id=" + getId() +
                ", fechaHora='" + getFechaHora() + "'" +
                ", motivo='" + getMotivo() + "'" +
                ", estado='" + getEstado() + "'" +
                ", enlaceTelemedicina='" + getEnlaceTelemedicina() + "'" +
                ", costo=" + getCosto() +
                ", pagado='" + getPagado() + "'" +
                ", activo='" + getActivo() + "'" +
                "}";
    }
}
