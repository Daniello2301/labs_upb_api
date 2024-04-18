package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Activo;
import co.edu.upb.labs_upb.model.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface for the Prestamo entity.
 * This interface extends JpaRepository and provides methods for querying the Prestamo table.
 */
@Repository
public interface IPrestamoRepository extends JpaRepository<Prestamo, Long> {

    /**
     * Closes a Prestamo by its id.
     *
     * @param id The id of the Prestamo
     */
    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "UPDATE prestamos p SET p.estado = 0 WHERE p.id_prestamo = ?1"
    )
    void closePrestamo(Long id);

    /**
     * Finds Prestamos by their estado.
     *
     * @param estado The estado of the Prestamo
     * @param pageable The pagination information
     * @return A page of Prestamo objects
     */
    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM Prestamos p " +
                    "WHERE p.estado = ?1")
    Page<Prestamo> finByEstado(boolean estado, Pageable pageable);

    /**
     * Finds a Prestamo by its numeroPrestamo.
     *
     * @param numeroPrestamo The numeroPrestamo of the Prestamo
     * @return The Prestamo object
     */
    Prestamo findByNumeroPrestamo(Long numeroPrestamo);


}
