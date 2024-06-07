package co.edu.upb.labs_upb.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "reservas_aulas")
@Data
public class ReservaDeAula implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_aula")
    private Aula aula;

    private Long bloque;

    @OneToMany(mappedBy = "reservaDeAula")
    private Set<FechaReserva> fechasReserva;

    private String persona;
    private String descripcion;
    private Boolean estado;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
