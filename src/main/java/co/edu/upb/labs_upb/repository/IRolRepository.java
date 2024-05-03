package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * IRolRepository is an interface that extends JpaRepository to provide CRUD operations for the Rol entity.
 * It is annotated with @Repository to indicate that it's a Bean and its role is to interact with the database.
 */
@Repository
public interface IRolRepository extends JpaRepository<Rol, Long> {

    /**
     * Method to find a Rol entity by its name.
     *
     * @param nombre the name of the Rol entity to be searched.
     * @return the Rol entity that matches the given name.
     */
    Rol findByNombre(String nombre);
}
