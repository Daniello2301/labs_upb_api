package co.edu.upb.labs_upb.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    int numero;

    String descripcion;

    @OneToOne(mappedBy = "bloque")
    Aula aula;

    @Column(name = "fecha_creacion", nullable = false)
    LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    LocalDateTime fechaActualizacion;
}
