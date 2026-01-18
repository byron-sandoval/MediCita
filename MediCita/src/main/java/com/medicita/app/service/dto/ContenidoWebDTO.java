package com.medicita.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.medicita.app.domain.ContenidoWeb} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContenidoWebDTO implements Serializable {

    private Long id;

    @NotNull
    private String clave;

    private String valorTexto;

    @Lob
    private byte[] imagen;

    private String imagenContentType;

    @NotNull
    private Boolean activo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValorTexto() {
        return valorTexto;
    }

    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return imagenContentType;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContenidoWebDTO)) {
            return false;
        }

        ContenidoWebDTO contenidoWebDTO = (ContenidoWebDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contenidoWebDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContenidoWebDTO{" +
            "id=" + getId() +
            ", clave='" + getClave() + "'" +
            ", valorTexto='" + getValorTexto() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
