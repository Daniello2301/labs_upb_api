package co.edu.upb.labs_upb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "imagenes_activos")
@Data
public class ImagenActivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long id;

    @OneToOne(mappedBy = "imagen")
    @JoinColumn(name = "id_activo")
    private Activo activo;

    @Lob
    private byte[] imagen;

}
