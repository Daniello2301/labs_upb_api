package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * IUsuarioRepository is an interface that extends JpaRepository to provide CRUD operations for the Usuario entity.
 * It is annotated with @Repository to indicate that it's a Bean and its role is to interact with the database.
 */
@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>
{
    /**
     * Method to find a Usuario entity by its email.
     *
     * @param correo the email of the Usuario entity to be searched.
     * @return an Optional<Usuario> that contains the Usuario entity if it exists, or empty if it doesn't.
     */
    Optional<Usuario> findByEmail(String correo);

    /**
     * Method to find a Usuario entity by its document number.
     *
     * @param documento the document number of the Usuario entity to be searched.
     * @return the Usuario entity that matches the given document number.
     */
    Usuario findByDocumento(Long documento);

    /**
     * Method to find a Usuario entity by its UPB ID.
     *
     * @param idUpb the UPB ID of the Usuario entity to be searched.
     * @return the Usuario entity that matches the given UPB ID.
     */
    Usuario findByIdUpb(Long idUpb);

}
