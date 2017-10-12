package com.worknest.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Cajeros.
 */
@Entity
@Table(name = "cajeros")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cajeros implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "id_cajero", nullable = false)
    private Long idCajero;

    @NotNull
    @Size(max = 50)
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 50)
    @Column(name = "apellidos", length = 50, nullable = false)
    private String apellidos;

    @DecimalMax(value = "50")
    @Column(name = "fondo")
    private Float fondo;

    @OneToOne
    @JoinColumn(unique = true)
    private Cajas cajas;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCajero() {
        return idCajero;
    }

    public Cajeros idCajero(Long idCajero) {
        this.idCajero = idCajero;
        return this;
    }

    public void setIdCajero(Long idCajero) {
        this.idCajero = idCajero;
    }

    public String getNombre() {
        return nombre;
    }

    public Cajeros nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public Cajeros apellidos(String apellidos) {
        this.apellidos = apellidos;
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Float getFondo() {
        return fondo;
    }

    public Cajeros fondo(Float fondo) {
        this.fondo = fondo;
        return this;
    }

    public void setFondo(Float fondo) {
        this.fondo = fondo;
    }

    public Cajas getCajas() {
        return cajas;
    }

    public Cajeros cajas(Cajas cajas) {
        this.cajas = cajas;
        return this;
    }

    public void setCajas(Cajas cajas) {
        this.cajas = cajas;
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
        Cajeros cajeros = (Cajeros) o;
        if (cajeros.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cajeros.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cajeros{" +
            "id=" + getId() +
            ", idCajero='" + getIdCajero() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", fondo='" + getFondo() + "'" +
            "}";
    }
}
