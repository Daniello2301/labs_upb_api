package co.edu.upb.labs_upb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;


/**
 * The TipoActivo class represents an equipment type in the system.
 * It is a JPA entity class; a table for this class will be created in the database.
 * It uses Lombok annotations for boilerplate code reduction.
 */
@Entity
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "tipos_activos")
public class TipoActivo implements Serializable {

    /**
     * The unique ID of the asset type.
     * It is generated automatically when an equipment type is created.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_activo")
    Long id;

    @Column(name = "nomenclatura",length = 100, nullable = false, unique = true)
    String nomenclatura;

    @Column(name = "descripcion",length = 200)
    String  descripcion;

    @Column(name = "fecha_creacion", updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    LocalDateTime fechaActualizacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoActivo")
    Set<Activo> activos;
}
