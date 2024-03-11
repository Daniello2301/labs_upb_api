package co.edu.upb.labs_upb.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_bloque", nullable = false)
    Bloque bloque;

    @Column(name = "fecha_creacion", nullable = false)
    LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    LocalDateTime fechaActualizacion;

}
