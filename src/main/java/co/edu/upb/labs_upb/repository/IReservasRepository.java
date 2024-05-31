package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.dto.ReservasAulaDTO;
import co.edu.upb.labs_upb.model.ReservaDeAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IReservasRepository extends JpaRepository<ReservaDeAula, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM reservas_aula WHERE id_aula = (?1)")
    Set<ReservaDeAula> findByAula(Long idAula);
}
