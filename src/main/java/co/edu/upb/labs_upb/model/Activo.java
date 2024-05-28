package co.edu.upb.labs_upb.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Activo is an entity class representing an asset in the system.
 * It is annotated with @Entity to indicate that it is a JPA entity.
 * The @Table annotation specifies the details of the table that will be used to create the table in the database.
 * It is also annotated with @Data from Lombok to automatically generate getters, setters, equals, hashCode and toString methods.
 * The @FieldDefaults annotation is used to set the default visibility of the fields in this class.
 */
@Entity
@Table(name = "activos")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Activo implements Serializable {

    /**
     * Unique identifier for the Activo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_activo", nullable = false, unique = true)
    private Long id;

    @Column(name = "numero_inventario", nullable = false, unique = true)
    private String numeroInventario;

    @Column(name = "serial", nullable = false, unique = true)
    private String serial;

    private String modelo;

    private String descripcion;

    private Boolean estado;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_imagen")
    private ImagenActivo imagen;

    /**
     * This field represents the type of the asset (e.g., computer, furniture) associated with the current entity.
     * It is a many-to-one relationship with the `TipoActivo` entity. This means that a single instance of this entity
     * can be associated with one `TipoActivo` entity.
     *
     * @ManyToOne specifies a many-to-one relationship with the `TipoActivo` entity.
     *
     * @JoinColumn(name = "id_tipo_activo") defines the join column in the current entity's table that references the primary key
     *   of the `TipoActivo` entity. In this case, the join column is named "id_tipo_activo".
     *
     * @see TipoActivo (reference to the associated entity class)
     */
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinColumn(name = "id_tipo_activo")
    private TipoActivo tipoActivo;


    /**
     * This field represents the loan (Prestamo) associated with the current entity.
     * It is a many-to-one relationship with the `Prestamo` entity. This means that a single instance of this entity
     * can be associated with one `Prestamo` entity (e.g., an asset can be part of only one loan at a time).
     *
     * @ManyToOne specifies a many-to-one relationship with the `Prestamo` entity.
     *
     * @JoinColumn(name = "id_prestamo") defines the join column in the current entity's table that references the primary key
     *   of the `Prestamo` entity. In this case, the join column is named "id_prestamo".
     *
     * @see Prestamo (reference to the associated entity class)
     */
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinColumn(name = "id_prestamo")
    private Prestamo prestamo;


    /**
     * This field represents the classroom (Aula) associated with the current entity.
     * It is a many-to-one relationship with the `Aula` entity. This means that a single instance of this entity
     * can be associated with one `Aula` entity (e.g., an asset can be assigned to a specific classroom).
     *
     * @ManyToOne specifies a many-to-one relationship with the `Aula` entity.
     *
     * @JoinColumn(name = "id_aula") defines the join column in the current entity's table that references the primary key
     *   of the `Aula` entity. In this case, the join column is named "id_aula".
     *
     * @see Aula (reference to the associated entity class)
     */
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinColumn(name = "id_aula")
    private Aula aula;


    /**
     * This field represents the building block (Bloque) associated with the current entity.
     * It is a many-to-one relationship with the `Bloque` entity. This means that a single instance of this entity
     * can be associated with one `Bloque` entity (e.g., a classroom can belong to only one building block).
     *
     * @ManyToOne specifies a many-to-one relationship with the `Bloque` entity.
     *
     * @JoinColumn(name = "id_bloque") defines the join column in the current entity's table that references the primary key
     *   of the `Bloque` entity. In this case, the join column is named "id_bloque".
     *
     * @see Bloque (reference to the associated entity class)
     */
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinColumn(name = "id_bloque")
    private Bloque bloque;

    /**
     * This field represents the user (Usuario) associated with the current entity.
     * It is a many-to-one relationship with the `Usuario` entity. This means that a single instance of this entity
     * can be associated with one `Usuario` entity (e.g., an asset can be assigned to a specific user).
     *
     * @ManyToOne specifies a many-to-one relationship with the `Usuario` entity.
     *
     * @JoinColumn(name = "id_usuario") defines the join column in the current entity's table that references the primary key
     *   of the `Usuario` entity. In this case, the join column is named "id_usuario".
     *
     * @see Usuario (reference to the associated entity class)
     */
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

}
