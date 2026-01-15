package com.medicita.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medicita.app.domain.enumeration.Genero;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private Genero genero;

    @NotNull
    @Column(name = "keycloak_id", nullable = false, unique = true)
    private String keycloakId;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cita", "historiaClinica" }, allowSetters = true)
    private Adjunto foto;

    @JsonIgnoreProperties(value = { "paciente" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "paciente")
    private HistoriaClinica historiaClinica;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paciente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Paciente telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Paciente fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Genero getGenero() {
        return this.genero;
    }

    public Paciente genero(Genero genero) {
        this.setGenero(genero);
        return this;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getKeycloakId() {
        return this.keycloakId;
    }

    public Paciente keycloakId(String keycloakId) {
        this.setKeycloakId(keycloakId);
        return this;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Paciente activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Adjunto getFoto() {
        return this.foto;
    }

    public void setFoto(Adjunto adjunto) {
        this.foto = adjunto;
    }

    public Paciente foto(Adjunto adjunto) {
        this.setFoto(adjunto);
        return this;
    }

    public HistoriaClinica getHistoriaClinica() {
        return this.historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        if (this.historiaClinica != null) {
            this.historiaClinica.setPaciente(null);
        }
        if (historiaClinica != null) {
            historiaClinica.setPaciente(this);
        }
        this.historiaClinica = historiaClinica;
    }

    public Paciente historiaClinica(HistoriaClinica historiaClinica) {
        this.setHistoriaClinica(historiaClinica);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return getId() != null && getId().equals(((Paciente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", telefono='" + getTelefono() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", genero='" + getGenero() + "'" +
            ", keycloakId='" + getKeycloakId() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
