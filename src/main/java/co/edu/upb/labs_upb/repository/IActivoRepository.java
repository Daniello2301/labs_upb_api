package co.edu.upb.labs_upb.repository;


import co.edu.upb.labs_upb.model.Activo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


/**
 * Repository interface for the Activo entity.
 * This interface extends JpaRepository and provides methods for querying the Activo table.
 */
@Repository
public interface IActivoRepository extends JpaRepository<Activo, Long> {


    /**
     * Finds an Activo by numeroInventario numeroInventario.
     *
     * @param numeroInventario The numeroInventario of the Activo
     * @return The Activo object
     */
    Activo  findByNumeroInventario(String numeroInventario);

    /*
    @Query(nativeQuery = true,
            value =
                    "SELECT * " +
                    "FROM Activos a " +
                    "WHERE a.estado = 1")
    Set<Activo> findAllEnable();
    */

    /**
     * Finds Activos by the idUpb of the associated Usuario.
     *
     * @param idUsuarioUpb The idUpb of the Usuario
     * @return A set of Activo objects
     */
    @Query(nativeQuery = true,
            value =
                    "SELECT " +
                        "a.id_activo, " +
                        "a.numero_inventario, " +
                        "a.descripcion, " +
                        "a.serial, " +
                        "a.modelo, " +
                        "a.estado, "+
                        "a.id_aula, " +
                        "a.id_usuario, " +
                        "a.id_tipo_activo, " +
                        "a.id_bloque, " +
                        "a.id_prestamo, " +
                        "a.fecha_actualizacion,"+
                        "a.fecha_creacion "+
                    "FROM Activos a " +
                    "INNER JOIN Usuarios u " +
                    "ON a.id_usuario = u.id_usuario " +
                    "WHERE u.id_upb = ?1")
    Set<Activo> findByUsuarioIdUpb(String idUsuarioUpb);


    /**
     * Finds Activos by their estado.
     *
     * @param estado The estado of the Activo
     * @param pageable The pagination information
     * @return A page of Activo objects
     */
    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM Activos a " +
                    "WHERE a.estado = ?1")
    Page<Activo> finByEstado(boolean estado, Pageable pageable);

    /**
     * Finds Activos by the idPrestamo.
     *
     * @param idPrestamo The idPrestamo of the Activo
     * @return A set of Activo objects
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM activos a WHERE a.id_prestamo = ?1")
    Set<Activo> findByIdPrestamo(Long idPrestamo);



    /**
     * Updates the idPrestamo of an Activo by numeroInventario.
     *
     * @param idPrestamo The new idPrestamo
     * @param numeroInventario The numeroInventario of the Activo
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true,
    value ="UPDATE activos a SET a.id_prestamo = ?1 WHERE a.numero_inventario = ?2")
    void updatePrestamo(Long idPrestamo, String numeroInventario);
}
