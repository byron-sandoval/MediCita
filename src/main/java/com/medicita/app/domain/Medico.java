package com.medicita.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medicita.app.domain.enumeration.Especialidad;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Medico.
 */
@Entity
@Table(name = "medico")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_licencia", nullable = false, unique = true)
    private String numeroLicencia;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "especialidad", nullable = false)
    private Especialidad especialidad;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "tarifa_consulta", precision = 21, scale = 2, nullable = false)
    private BigDecimal tarifaConsulta;

    @NotNull
    @Column(name = "keycloak_id", nullable = false, unique = true)
    private String keycloakId;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cita", "historiaClinica" }, allowSetters = true)
    private Adjunto foto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroLicencia() {
        return this.numeroLicencia;
    }

    public Medico numeroLicencia(String numeroLicencia) {
        this.setNumeroLicencia(numeroLicencia);
        return this;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public Especialidad getEspecialidad() {
        return this.especialidad;
    }

    public Medico especialidad(Especialidad especialidad) {
        this.setEspecialidad(especialidad);
        return this;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public BigDecimal getTarifaConsulta() {
        return this.tarifaConsulta;
    }

    public Medico tarifaConsulta(BigDecimal tarifaConsulta) {
        this.setTarifaConsulta(tarifaConsulta);
        return this;
    }

    public void setTarifaConsulta(BigDecimal tarifaConsulta) {
        this.tarifaConsulta = tarifaConsulta;
    }

    public String getKeycloakId() {
        return this.keycloakId;
    }

    public Medico keycloakId(String keycloakId) {
        this.setKeycloakId(keycloakId);
        return this;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Medico activo(Boolean activo) {
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

    public Medico foto(Adjunto adjunto) {
        this.setFoto(adjunto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medico)) {
            return false;
        }
        return getId() != null && getId().equals(((Medico) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medico{" +
            "id=" + getId() +
            ", numeroLicencia='" + getNumeroLicencia() + "'" +
            ", especialidad='" + getEspecialidad() + "'" +
            ", tarifaConsulta=" + getTarifaConsulta() +
            ", keycloakId='" + getKeycloakId() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
