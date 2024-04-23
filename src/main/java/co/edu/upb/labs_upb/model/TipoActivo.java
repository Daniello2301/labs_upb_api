package co.edu.upb.labs_upb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
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

    private final int lengthNomenclatura = 50;
    private final int lengthDescription = 200;
    /**
     * The unique ID of the asset type.
     * It is generated automatically when an equipment type is created.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_activo")
    private Long id;

    @Column(name = "nomenclatura", length = lengthNomenclatura, nullable = false, unique = true)
    private String nomenclatura;

    @Column(name = "descripcion",  length = lengthDescription)
    private String  descripcion;

    @Column(name = "fecha_creacion", updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoActivo")
    private Set<Activo> activos;
}
