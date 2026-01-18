package com.medicita.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medicita.app.domain.enumeration.Genero;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
@SQLDelete(sql = "UPDATE paciente SET activo = false WHERE id = ?")
@Where(clause = "activo = true")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotNull
    @Column(name = "cedula", nullable = false, unique = true)
    private String cedula;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

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

    public String getNombre() {
        return this.nombre;
    }

    public Paciente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Paciente apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return this.cedula;
    }

    public Paciente cedula(String cedula) {
        this.setCedula(cedula);
        return this;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getEmail() {
        return this.email;
    }

    public Paciente email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

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
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + "'" +
                ", apellido='" + getApellido() + "'" +
                ", cedula='" + getCedula() + "'" +
                ", email='" + getEmail() + "'" +
                ", telefono='" + getTelefono() + "'" +
                ", fechaNacimiento='" + getFechaNacimiento() + "'" +
                ", genero='" + getGenero() + "'" +
                ", keycloakId='" + getKeycloakId() + "'" +
                ", activo='" + getActivo() + "'" +
                "}";
    }
}
