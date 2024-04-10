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

@Repository
public interface IPrestamoRepository extends JpaRepository<Prestamo, Long> {


    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "UPDATE prestamos p SET p.estado = 0 WHERE p.id_prestamo = ?1"
    )
    void closePrestamo(Long id);


    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM Prestamos p " +
                    "WHERE p.estado = ?1")
    Page<Prestamo> finByEstado(boolean estado, Pageable pageable);
    Prestamo findByNumeroPrestamo(Long numeroPrestamo);


}
