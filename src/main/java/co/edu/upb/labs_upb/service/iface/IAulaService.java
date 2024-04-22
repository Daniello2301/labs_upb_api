package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.AulaDTO;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Aula;

import java.util.List;

/**
 * IAulaService is an interface that defines the contract for the Aula service.
 * It includes methods for CRUD operations.
 */
public interface IAulaService {

    // CRUD methods

    /**
     * Retrieves all AulaDTO objects.
     *
     * @return a List of AulaDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    public List<AulaDTO> findAllAulas() throws RestException;

    /**
     * Retrieves an AulaDTO object by its ID.
     *
     * @param id the ID of the AulaDTO to retrieve.
     * @return an AulaDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    public AulaDTO findAulaById(Long id) throws RestException;

    /**
     * Retrieves AulaDTO objects by their number.
     *
     * @param numero the number of the AulaDTO objects to retrieve.
     * @return a List of AulaDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    List<AulaDTO> findAulaByNumero(Long numero) throws RestException;

    /**
     * Creates a new AulaDTO object.
     *
     * @param aula the AulaDTO object to create.
     * @return the created AulaDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    public AulaDTO saveAula(AulaDTO aula) throws RestException;

    /**
     * Deletes an AulaDTO object by its ID.
     *
     * @param id the ID of the AulaDTO to delete.
     * @throws RestException if an error occurs during the operation.
     */
    public void deleteAula(Long id) throws RestException;

}
