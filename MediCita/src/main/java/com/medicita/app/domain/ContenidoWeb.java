package com.medicita.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A ContenidoWeb.
 */
@Entity
@Table(name = "contenido_web")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContenidoWeb implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "clave", nullable = false, unique = true)
    private String clave;

    @Column(name = "valor_texto")
    private String valorTexto;

    @Lob
    @Column(name = "imagen")
    private byte[] imagen;

    @Column(name = "imagen_content_type")
    private String imagenContentType;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContenidoWeb id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return this.clave;
    }

    public ContenidoWeb clave(String clave) {
        this.setClave(clave);
        return this;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValorTexto() {
        return this.valorTexto;
    }

    public ContenidoWeb valorTexto(String valorTexto) {
        this.setValorTexto(valorTexto);
        return this;
    }

    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }

    public byte[] getImagen() {
        return this.imagen;
    }

    public ContenidoWeb imagen(byte[] imagen) {
        this.setImagen(imagen);
        return this;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return this.imagenContentType;
    }

    public ContenidoWeb imagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
        return this;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public ContenidoWeb activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContenidoWeb)) {
            return false;
        }
        return getId() != null && getId().equals(((ContenidoWeb) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContenidoWeb{" +
            "id=" + getId() +
            ", clave='" + getClave() + "'" +
            ", valorTexto='" + getValorTexto() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", imagenContentType='" + getImagenContentType() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
