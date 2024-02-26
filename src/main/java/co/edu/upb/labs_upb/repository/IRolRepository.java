package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);
}
