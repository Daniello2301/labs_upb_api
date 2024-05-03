package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.TipoActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ITipoActivoRepository is an interface that extends JpaRepository to provide CRUD operations for the TipoActivo entity.
 * It is annotated with @Repository to indicate that it's a Bean and its role is to interact with the database.
 */
@Repository
public interface ITipoActivoRepository extends JpaRepository<TipoActivo, Long> {

    /**
     * Method to find a TipoActivo entity by its nomenclature.
     *
     * @param nomenclatura the nomenclature of the TipoActivo entity to be searched.
     * @return the TipoActivo entity that matches the given nomenclature.
     */
    TipoActivo findByNomenclatura(String nomenclatura);

}
