package co.edu.upb.labs_upb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;


/**
 * The Prestamo class represents a loan in the system.
 * It is a JPA entity class; a table for this class will be created in the database.
 * It uses Lombok annotations for boilerplate code reduction.
 */
@Entity
@Table(name = "prestamos")
@Data
public class Prestamo implements Serializable {

    /**
     * The unique ID of the loan.
     * It is generated automatically when a loan is created.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo", nullable = false, unique = true)
    private Long id;

    @Column(name = "numero_prestamo", nullable = false, unique = true)
    private Long numeroPrestamo;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDateTime fechaSalida;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    private String laboratorio;

    @Column(name = "centro_costos", nullable = false)
    private String centroCostos;

    private String facultad;

    private Boolean estado;

    @Column(name = "id_persona", nullable = false)
    private String  idPersona;

    @Column(name = "nombre_persona", nullable = false)
    private String nombrePersona;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "prestamo", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Activo> activos;
}
