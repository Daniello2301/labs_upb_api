package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.ImagenActivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImagenActivo extends JpaRepository<ImagenActivo, Long> {

    ImagenActivo getByActivoId(Long id);
}
