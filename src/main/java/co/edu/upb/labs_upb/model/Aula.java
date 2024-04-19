package co.edu.upb.labs_upb.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Aula is an entity class representing a classroom in the system.
 * It is annotated with @Entity to indicate that it is a JPA entity.
 * The @Table annotation specifies the details of the table that will be used to create the table in the database.
 * It is also annotated with @Data from Lombok to automatically generate getters, setters, equals, hashCode and toString methods.
 * The @FieldDefaults annotation is used to set the default visibility of the fields in this class.
 */
@Entity
@Table(name = "aulas")
@Data
@FieldDefaults( level = AccessLevel.PRIVATE)
public class Aula implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula", nullable = false)
    Long id;

    @Column(name = "numero_aula", nullable = false)
    Long numero;

    String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    LocalDateTime fechaActualizacion;


    /**
     * This field represents the building block (Bloque) associated with the current entity.
     * It is a many-to-one relationship with the `Bloque` entity. This means that a single instance of this entity
     * can be associated with one `Bloque` entity (e.g., a classroom can belong to only one building block).
     *
     * @ManyToOne specifies a many-to-one relationship with the `Bloque` entity. By default, cascade types are not set,
     *  meaning persistence operations on the owning entity won't cascade to the associated entity.
     *
     * @JoinColumn(name = "id_bloque") defines the join column in the current entity's table that references the primary key
     *   of the `Bloque` entity. In this case, the join column is named "id_bloque".
     *s
     * @see Bloque (reference to the associated entity class)
     */
    @ManyToOne()
    @JoinColumn(name = "id_bloque")
    Bloque bloque;

}
