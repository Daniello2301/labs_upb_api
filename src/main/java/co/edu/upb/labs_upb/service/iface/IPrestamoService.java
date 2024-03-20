package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.PrestamoDTO;
import co.edu.upb.labs_upb.exception.RestException;

import java.util.List;

public interface IPrestamoService {

    // CRUD methods

    public List<PrestamoDTO> getAll() throws RestException;

    public PrestamoDTO getById(Long id) throws RestException;

    public PrestamoDTO create(PrestamoDTO prestamoDTO) throws RestException;

    public void deleteById(Long id) throws RestException;

}
