package co.edu.upb.labs_upb.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "bloques")
@FieldDefaults( level = AccessLevel.PRIVATE)
public class Bloque implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bloque", nullable = false)
    Long id;

    @Column(name = "numero_bloque", nullable = false, unique = true)
    Long numero;

    String descripcion;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bloque", cascade  = CascadeType.REMOVE)
//    List<Aula> aulas;

    @Column(name = "fecha_creacion", nullable = false)
    LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    LocalDateTime fechaActualizacion;
}
