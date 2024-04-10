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

@Repository
public interface IActivoRepository extends JpaRepository<Activo, Long> {

    Activo  findByNumeroInventario(String numeroInventario);

    /*
    @Query(nativeQuery = true,
            value =
                    "SELECT * " +
                    "FROM Activos a " +
                    "WHERE a.estado = 1")
    Set<Activo> findAllEnable();
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

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM Activos a " +
                    "WHERE a.estado = ?1")
    Page<Activo> finByEstado(boolean estado, Pageable pageable);

    @Query(nativeQuery = true,
            value = "SELECT * FROM activos a WHERE a.id_prestamo = ?1")
    Set<Activo> findByIdPrestamo(Long idPrestamo);


    @Modifying
    @Transactional
    @Query(nativeQuery = true,
    value ="UPDATE activos a SET a.id_prestamo = ?1 WHERE a.numero_inventario = ?2")
    void updatePrestamo(Long idPrestamo, String numeroInventario);
}
