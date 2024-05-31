package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.FechaReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IFechasReservasRepository extends JpaRepository<FechaReserva, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM fechas_reservas WHERE id_reserva = (?1)")
    Set<FechaReserva> findByIdReserva(Long idReserva);

    @Query(nativeQuery = true,
            value = "SELECT * FROM fechas_reservas WHERE fecha_reserva = (?1)")
    Set<FechaReserva> findbyFecha(String fecha);
}
