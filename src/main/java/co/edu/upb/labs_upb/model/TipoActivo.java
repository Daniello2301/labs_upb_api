package co.edu.upb.labs_upb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "tipos_activos")
public class TipoActivo implements Serializable {

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
