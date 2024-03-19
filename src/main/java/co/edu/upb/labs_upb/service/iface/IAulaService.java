package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.AulaDTO;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Aula;

import java.util.List;

public interface IAulaService {

    // CRUD methods

    public List<AulaDTO> findAllAulas() throws RestException;

    public AulaDTO findAulaById(Long id) throws RestException;

    List<AulaDTO> findAulaByNumero(Long numero) throws RestException;

    public AulaDTO saveAula(AulaDTO aula) throws RestException;

    public void deleteAula(Long id) throws RestException;

}
