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
    Long id;

    @Column(name = "numero_aula", nullable = false, unique = true)
    Long numero;

    String descripcion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bloque_id", referencedColumnName = "id_bloque")
    Bloque bloque;

    @Column(name = "fecha_creacion", nullable = false)
    LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    LocalDateTime fechaActualizacion;

}
