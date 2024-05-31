package co.edu.upb.labs_upb.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import lombok.Data;

import java.sql.Blob;

@Entity
@Table(name = "imagenes_activos")
@Data
public class ImagenActivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long id;

    @OneToOne()
    @JoinColumn(name = "id_activo")
    private Activo activo;

    @Lob
    private Blob imagen;

}
