package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.PrestamoDTO;
import co.edu.upb.labs_upb.exception.RestException;

import java.util.List;
import java.util.Map;

public interface IPrestamoService {

    // CRUD methods

    List<PrestamoDTO> getAll() throws RestException;

    PrestamoDTO getById(Long id) throws RestException;

    Map<String, Object> prestamosEnable(int page, int size, String sortby) throws RestException;

    PrestamoDTO create(PrestamoDTO prestamoDTO) throws RestException;

    void addActivo(Long id, String numeroInventario) throws RestException;

    void closePrestamo(Long id) throws RestException;

}
