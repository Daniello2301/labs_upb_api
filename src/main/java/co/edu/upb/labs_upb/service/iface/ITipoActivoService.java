package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.TipoActivoDTO;
import co.edu.upb.labs_upb.exception.RestException;

import java.util.List;

/**
 * ITipoActivoService is an interface that defines the contract for the TipoActivo service.
 * It includes methods for CRUD operations and other operations related to TipoActivo objects.
 */
public interface ITipoActivoService {

    /**
     * Retrieves all TipoActivoDTO objects.
     *
     * @return a List of TipoActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    List<TipoActivoDTO> findAllTipoActivo() throws RestException;

    /**
     * Retrieves a TipoActivoDTO object by its ID.
     *
     * @param id the ID of the TipoActivoDTO to retrieve.
     * @return a TipoActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    TipoActivoDTO findTipoActivoById(Long id) throws RestException;

    /**
     * Retrieves a TipoActivoDTO object by its nomenclatura.
     *
     * @param nomenclatura the nomenclatura of the TipoActivoDTO to retrieve.
     * @return a TipoActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    TipoActivoDTO findByNomenclatura(String nomenclatura) throws RestException;

    /**
     * Creates a new TipoActivoDTO object.
     *
     * @param tipoActivo the TipoActivoDTO object to create.
     * @return the created TipoActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    TipoActivoDTO saveTipoActivo(TipoActivoDTO tipoActivo) throws RestException;

    /**
     * Delete de TipoActivoDTO object by its ID.
     *
     * @param id the TipoActivoDTO object to create.
     * @throws RestException if an error occurs during the operation.
     */
    void deleteTipoActivo(Long id) throws RestException;

}
