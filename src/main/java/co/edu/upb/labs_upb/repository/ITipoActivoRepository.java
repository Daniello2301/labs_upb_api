package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.TipoActivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoActivoRepository extends JpaRepository<TipoActivo, Long> {

    public TipoActivo findByNomenclatura(String nomenclatura);

}
