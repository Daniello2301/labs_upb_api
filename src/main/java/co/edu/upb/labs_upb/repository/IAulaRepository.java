package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IAulaRepository extends JpaRepository<Aula, Long>{
    List<Aula> findByNumero(Long numero);


    @Query(nativeQuery = true,
    value = "SELECT " +
            "a.id_aula, " +
            "a.numero_aula, " +
            "a.descripcion, " +
            "a.fecha_creacion, " +
            "a.fecha_actualizacion, " +
            "a.id_bloque " +
            "FROM aulas a " +
            "INNER JOIN bloques b " +
            "ON a.id_bloque = b.id_bloque " +
            "WHERE a.numero_aula = (?1) AND b.id_bloque = (?2)")
    Aula findByNumeroInTheSameBloque(Long aula, Long bloque);

}
