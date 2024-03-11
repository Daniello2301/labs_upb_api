package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.TipoActivoDTO;
import co.edu.upb.labs_upb.exception.RestException;

import java.util.List;

public interface ITipoActivoService {

    // Crud methods

    public List<TipoActivoDTO> findAllTipoActivo() throws RestException;

    public TipoActivoDTO findTipoActivoById(Long id) throws RestException;

    public TipoActivoDTO findByNomenclatura(String nomenclatura) throws RestException;

    public TipoActivoDTO saveTipoActivo(TipoActivoDTO tipoActivo) throws RestException;

    public void deleteTipoActivo(Long id) throws RestException;

}
