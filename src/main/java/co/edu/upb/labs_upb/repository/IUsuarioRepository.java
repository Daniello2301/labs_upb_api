package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>
{
    public Usuario findByCorreo(String correo);

    Usuario findByDocumento(Long documento);

    Usuario findByIdUpb(Long idUpb);

}
