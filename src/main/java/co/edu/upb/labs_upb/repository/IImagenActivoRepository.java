package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.ImagenActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImagenActivoRepository extends JpaRepository<ImagenActivo, Long> {

    ImagenActivo getByActivoId(Long id);
}
