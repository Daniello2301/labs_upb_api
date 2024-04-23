package co.edu.upb.labs_upb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * The Bloque class represents a block in the system.
 * It is a JPA entity class; a table for this class will be created in the database.
 * It uses Lombok annotations for boilerplate code reduction.
 */
@Data
@Entity
@Table(name = "bloques")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bloque implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bloque", nullable = false)
    private Long id;

    @Column(name = "numero_bloque", nullable = false, unique = true)
    private Long numero;

    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
}
