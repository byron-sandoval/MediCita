package com.medicita.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medicita.app.domain.enumeration.EstadoPago;
import com.medicita.app.domain.enumeration.MetodoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * A Pago.
 */
@Entity
@Table(name = "pago")
@SQLDelete(sql = "UPDATE pago SET activo = false WHERE id = ?")
@Where(clause = "activo = true")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_pago", nullable = false)
    private Instant fechaPago;

    @NotNull
    @Column(name = "monto", precision = 21, scale = 2, nullable = false)
    private BigDecimal monto;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", nullable = false)
    private MetodoPago metodo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPago estado;

    @Column(name = "transaccion_id")
    private String transaccionId;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "medico", "paciente" }, allowSetters = true)
    private Cita cita;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pago id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaPago() {
        return this.fechaPago;
    }

    public Pago fechaPago(Instant fechaPago) {
        this.setFechaPago(fechaPago);
        return this;
    }

    public void setFechaPago(Instant fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public Pago monto(BigDecimal monto) {
        this.setMonto(monto);
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public MetodoPago getMetodo() {
        return this.metodo;
    }

    public Pago metodo(MetodoPago metodo) {
        this.setMetodo(metodo);
        return this;
    }

    public void setMetodo(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public EstadoPago getEstado() {
        return this.estado;
    }

    public Pago estado(EstadoPago estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public String getTransaccionId() {
        return this.transaccionId;
    }

    public Pago transaccionId(String transaccionId) {
        this.setTransaccionId(transaccionId);
        return this;
    }

    public void setTransaccionId(String transaccionId) {
        this.transaccionId = transaccionId;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Pago activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Cita getCita() {
        return this.cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Pago cita(Cita cita) {
        this.setCita(cita);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pago)) {
            return false;
        }
        return getId() != null && getId().equals(((Pago) o).getId());
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
        return "Pago{" +
                "id=" + getId() +
                ", fechaPago='" + getFechaPago() + "'" +
                ", monto=" + getMonto() +
                ", metodo='" + getMetodo() + "'" +
                ", estado='" + getEstado() + "'" +
                ", transaccionId='" + getTransaccionId() + "'" +
                ", activo='" + getActivo() + "'" +
                "}";
    }
}
