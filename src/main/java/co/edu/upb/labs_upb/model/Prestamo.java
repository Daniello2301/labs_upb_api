package co.edu.upb.labs_upb.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "prestamos")
@Data
@FieldDefaults( level = AccessLevel.PRIVATE)
public class Prestamo implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo", nullable = false, unique = true)
    Long id;

    @Column(name = "numero_prestamo", nullable = false, unique = true)
    Long numeroPrestamo;

    @Column(name = "fecha_salida", nullable = false)
    LocalDateTime fechaSalida;

    @Column(name = "fecha_entrega")
    LocalDateTime fechaEntrega;

    String laboratorio;

    @Column(name = "centro_costos", nullable = false)
    String centroCostos;

    String facultad;

    Boolean estado;

    @Column(name = "id_persona", nullable = false)
    String  idPersona;

    @Column(name = "nombre_persona", nullable = false)
    String nombrePersona;

    @Column( name = "fecha_creacion", nullable = false)
    LocalDateTime fechaCreacion;

    @Column( name = "fecha_actualizacion", nullable = false)
    LocalDateTime fechaActualizacion;

    @OneToMany( fetch = FetchType.LAZY, mappedBy = "prestamo", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    Set<Activo> activos;
}
