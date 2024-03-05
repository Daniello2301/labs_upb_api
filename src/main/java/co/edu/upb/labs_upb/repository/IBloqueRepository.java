package co.edu.upb.labs_upb.repository;

import co.edu.upb.labs_upb.model.Bloque;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IBloqueRepository extends JpaRepository<Bloque, Long>{
}
