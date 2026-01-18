package com.medicita.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * A HistoriaClinica.
 */
@Entity
@Table(name = "historia_clinica")
@SQLDelete(sql = "UPDATE historia_clinica SET activo = false WHERE id = ?")
@Where(clause = "activo = true")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriaClinica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    @NotNull
    @Size(max = 5000)
    @Column(name = "diagnostico", length = 5000, nullable = false)
    private String diagnostico;

    @Size(max = 5000)
    @Column(name = "tratamiento", length = 5000)
    private String tratamiento;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "foto" }, allowSetters = true)
    private Paciente paciente;

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = Instant.now();
        }
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriaClinica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public HistoriaClinica fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDiagnostico() {
        return this.diagnostico;
    }

    public HistoriaClinica diagnostico(String diagnostico) {
        this.setDiagnostico(diagnostico);
        return this;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return this.tratamiento;
    }

    public HistoriaClinica tratamiento(String tratamiento) {
        this.setTratamiento(tratamiento);
        return this;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public HistoriaClinica activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public HistoriaClinica paciente(Paciente paciente) {
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
        if (!(o instanceof HistoriaClinica)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoriaClinica) o).getId());
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
        return "HistoriaClinica{" +
                "id=" + getId() +
                ", fechaCreacion='" + getFechaCreacion() + "'" +
                ", diagnostico='" + getDiagnostico() + "'" +
                ", tratamiento='" + getTratamiento() + "'" +
                ", activo='" + getActivo() + "'" +
                "}";
    }
}
