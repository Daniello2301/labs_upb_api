package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Bloque;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for the Bloque entity.
 * This interface extends JpaRepository and provides methods for querying the Bloque table.
 */
@Repository
public interface IBloqueRepository extends JpaRepository<Bloque, Long>{

    /**
     * Finds a Bloque by its numero.
     *
     * @param numero The numero of the Bloque
     * @return The Bloque object
     */
    public Bloque findByNumero(Long numero);
}
