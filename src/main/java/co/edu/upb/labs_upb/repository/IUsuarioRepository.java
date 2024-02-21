package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>
{
    Usuario findByCorreo(String correo);

    Usuario findByDocumento(Long documento);

    Usuario findByCorreoAndContrasena(String correo, String contrasena);

    Usuario findByDocumentoAndContrasena(Long documento, String contrasena);

}
