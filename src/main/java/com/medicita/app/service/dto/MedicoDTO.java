package com.medicita.app.service.dto;

import com.medicita.app.domain.enumeration.Especialidad;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.medicita.app.domain.Medico} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

    @NotNull
    private String email;

    @NotNull
    private String numeroLicencia;

    @NotNull
    private Especialidad especialidad;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal tarifaConsulta;

    @NotNull
    private String keycloakId;

    @NotNull
    private Boolean activo;

    private AdjuntoDTO foto;

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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public BigDecimal getTarifaConsulta() {
        return tarifaConsulta;
    }

    public void setTarifaConsulta(BigDecimal tarifaConsulta) {
        this.tarifaConsulta = tarifaConsulta;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public AdjuntoDTO getFoto() {
        return foto;
    }

    public void setFoto(AdjuntoDTO foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicoDTO)) {
            return false;
        }

        MedicoDTO medicoDTO = (MedicoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", email='" + getEmail() + "'" +
            ", numeroLicencia='" + getNumeroLicencia() + "'" +
            ", especialidad='" + getEspecialidad() + "'" +
            ", tarifaConsulta=" + getTarifaConsulta() +
            ", keycloakId='" + getKeycloakId() + "'" +
            ", activo='" + getActivo() + "'" +
            ", foto=" + getFoto() +
            "}";
    }
}
