package com.medicita.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Entidad para subir imágenes (perfil, recetas, estudios)
 */
@Entity
@Table(name = "adjunto")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Adjunto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Lob
    @Column(name = "contenido", nullable = false)
    private byte[] contenido;

    @NotNull
    @Column(name = "contenido_content_type", nullable = false)
    private String contenidoContentType;

    @Column(name = "tipo_contenido")
    private String tipoContenido;

    @Column(name = "fecha_subida")
    private Instant fechaSubida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "medico", "paciente" }, allowSetters = true)
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "paciente" }, allowSetters = true)
    private HistoriaClinica historiaClinica;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Adjunto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Adjunto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return this.contenido;
    }

    public Adjunto contenido(byte[] contenido) {
        this.setContenido(contenido);
        return this;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    public String getContenidoContentType() {
        return this.contenidoContentType;
    }

    public Adjunto contenidoContentType(String contenidoContentType) {
        this.contenidoContentType = contenidoContentType;
        return this;
    }

    public void setContenidoContentType(String contenidoContentType) {
        this.contenidoContentType = contenidoContentType;
    }

    public String getTipoContenido() {
        return this.tipoContenido;
    }

    public Adjunto tipoContenido(String tipoContenido) {
        this.setTipoContenido(tipoContenido);
        return this;
    }

    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public Instant getFechaSubida() {
        return this.fechaSubida;
    }

    public Adjunto fechaSubida(Instant fechaSubida) {
        this.setFechaSubida(fechaSubida);
        return this;
    }

    public void setFechaSubida(Instant fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public Cita getCita() {
        return this.cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Adjunto cita(Cita cita) {
        this.setCita(cita);
        return this;
    }

    public HistoriaClinica getHistoriaClinica() {
        return this.historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public Adjunto historiaClinica(HistoriaClinica historiaClinica) {
        this.setHistoriaClinica(historiaClinica);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Adjunto)) {
            return false;
        }
        return getId() != null && getId().equals(((Adjunto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Adjunto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", contenido='" + getContenido() + "'" +
            ", contenidoContentType='" + getContenidoContentType() + "'" +
            ", tipoContenido='" + getTipoContenido() + "'" +
            ", fechaSubida='" + getFechaSubida() + "'" +
            "}";
    }
}
