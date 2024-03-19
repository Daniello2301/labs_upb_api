package co.edu.upb.labs_upb.repository;


import co.edu.upb.labs_upb.model.Activo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IActivoRepository extends JpaRepository<Activo, Long> {

    Activo  findByNumeroInventario(String numeroInventario);


    @Query(nativeQuery = true,
            value =
                    "SELECT " +
                        "a.id_activo, " +
                        "a.numero_inventario, " +
                        "a.descripcion, " +
                        "a.serial, " +
                        "a.modelo, " +
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

}
