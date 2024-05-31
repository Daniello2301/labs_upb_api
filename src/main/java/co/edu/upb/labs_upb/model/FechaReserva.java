package co.edu.upb.labs_upb.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "fechas_reservas")
@Data
public class FechaReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fecha_de_reserva")
    private Long id;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime horaInicio;

    @Column(name = "fecha_fin_reserva", nullable = false)
    private LocalDateTime horaFin;

    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private ReservaDeAula reservaDeAula;
}
