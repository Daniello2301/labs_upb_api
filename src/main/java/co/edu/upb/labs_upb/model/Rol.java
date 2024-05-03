package co.edu.upb.labs_upb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * The Rol class represents a role in the system.
 * It is a JPA entity class; a table for this class will be created in the database.
 * It uses Lombok annotations for boilerplate code reduction.
 */
@Entity
@Table(name = "roles")
@Data
public class Rol {

    private final int length = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = length, unique = true)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;


}
