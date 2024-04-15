package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>
{
    Optional<Usuario> findByEmail(String correo);

    Usuario findByDocumento(Long documento);

    Usuario findByIdUpb(Long idUpb);

}
