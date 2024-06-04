package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.ReservaDeAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IReservasRepository extends JpaRepository<ReservaDeAula, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM reservas_aula WHERE id_aula = (?1)")
    Set<ReservaDeAula> findByAula(Long idAula);


    @Query(nativeQuery = true,
            value = """
                    select\s
                    ra.id_reserva,
                    ra.persona,
                    ra.descripcion,
                    ra.bloque,
                    ra.id_aula,
                    ra.estado,
                    ra.fecha_actualizacion,
                    ra.fecha_creacion
                    from fechas_reservas fr\s
                    join reservas_aulas ra\s
                    on fr.id_reserva = ra.id_reserva
                    inner join aulas a\s
                    on a.id_aula = ra.id_aula
                    where fr.fecha_reserva = (?1)
                    and a.numero_aula = (?2);
            """)
    List<ReservaDeAula> getByDateAndAula(String date, Long aula);
}
