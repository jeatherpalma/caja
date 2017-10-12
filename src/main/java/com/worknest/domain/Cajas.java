package com.worknest.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.worknest.domain.enumeration.Status;

/**
 * A Cajas.
 */
@Entity
@Table(name = "cajas")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cajas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = false)
    private Status estatus;

    @NotNull
    @Column(name = "sucursal", nullable = false)
    private Long sucursal;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Cajas nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Status getEstatus() {
        return estatus;
    }

    public Cajas estatus(Status estatus) {
        this.estatus = estatus;
        return this;
    }

    public void setEstatus(Status estatus) {
        this.estatus = estatus;
    }

    public Long getSucursal() {
        return sucursal;
    }

    public Cajas sucursal(Long sucursal) {
        this.sucursal = sucursal;
        return this;
    }

    public void setSucursal(Long sucursal) {
        this.sucursal = sucursal;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cajas cajas = (Cajas) o;
        if (cajas.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cajas.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cajas{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", estatus='" + getEstatus() + "'" +
            ", sucursal='" + getSucursal() + "'" +
            "}";
    }
}
