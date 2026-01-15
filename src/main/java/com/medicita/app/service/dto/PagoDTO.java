package com.medicita.app.service.dto;

import com.medicita.app.domain.enumeration.EstadoPago;
import com.medicita.app.domain.enumeration.MetodoPago;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.medicita.app.domain.Pago} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PagoDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant fechaPago;

    @NotNull
    private BigDecimal monto;

    @NotNull
    private MetodoPago metodo;

    @NotNull
    private EstadoPago estado;

    private String transaccionId;

    @NotNull
    private Boolean activo;

    private CitaDTO cita;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Instant fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public MetodoPago getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public String getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(String transaccionId) {
        this.transaccionId = transaccionId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public CitaDTO getCita() {
        return cita;
    }

    public void setCita(CitaDTO cita) {
        this.cita = cita;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PagoDTO)) {
            return false;
        }

        PagoDTO pagoDTO = (PagoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pagoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagoDTO{" +
            "id=" + getId() +
            ", fechaPago='" + getFechaPago() + "'" +
            ", monto=" + getMonto() +
            ", metodo='" + getMetodo() + "'" +
            ", estado='" + getEstado() + "'" +
            ", transaccionId='" + getTransaccionId() + "'" +
            ", activo='" + getActivo() + "'" +
            ", cita=" + getCita() +
            "}";
    }
}
