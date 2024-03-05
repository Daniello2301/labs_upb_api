package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IAulaRepository extends JpaRepository<Aula, Long>{
}
