package com.worknest.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A HistorialAcciones.
 */
@Entity
@Table(name = "historial_acciones")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HistorialAcciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "nombre_persona", length = 150, nullable = false)
    private String nombrePersona;

    @Size(max = 30)
    @Column(name = "nombre_caja", length = 30)
    private String nombreCaja;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Size(max = 100)
    @Column(name = "accion", length = 100, nullable = false)
    private String accion;

    @Size(max = 150)
    @Column(name = "descripcion", length = 150)
    private String descripcion;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public HistorialAcciones nombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
        return this;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getNombreCaja() {
        return nombreCaja;
    }

    public HistorialAcciones nombreCaja(String nombreCaja) {
        this.nombreCaja = nombreCaja;
        return this;
    }

    public void setNombreCaja(String nombreCaja) {
        this.nombreCaja = nombreCaja;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public HistorialAcciones fecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getAccion() {
        return accion;
    }

    public HistorialAcciones accion(String accion) {
        this.accion = accion;
        return this;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public HistorialAcciones descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        HistorialAcciones historialAcciones = (HistorialAcciones) o;
        if (historialAcciones.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historialAcciones.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistorialAcciones{" +
            "id=" + getId() +
            ", nombrePersona='" + getNombrePersona() + "'" +
            ", nombreCaja='" + getNombreCaja() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", accion='" + getAccion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
