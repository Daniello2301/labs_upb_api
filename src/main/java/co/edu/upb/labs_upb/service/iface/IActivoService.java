package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.ActivoDTO;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Activo;
import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IActivoService {

    Page<ActivoDTO> activosPagination(int numPage, int sizePage) throws RestException;

    //Sort by
    Map<String, Object> activosEnable(int page, int size, String sortby) throws RestException;

    //Sort by and pagination
    Page<ActivoDTO> activosPaginationSortBy(int numPage, int sizePage, String sortBy) throws RestException;

    // CRUD methods

    List<ActivoDTO> getAll() throws RestException;

    Set<ActivoDTO>  getByUsuarioId(String idUsuarioUpb) throws RestException;
    ActivoDTO getById(Long id) throws RestException;

    ActivoDTO create(ActivoDTO activoDTO) throws RestException;

    void deleteById(Long id) ;

}
