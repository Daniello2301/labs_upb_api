package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.PrestamoDTO;
import co.edu.upb.labs_upb.exception.RestException;

import java.util.List;
import java.util.Map;

/**
 * IPrestamoService is an interface that defines the contract for the Prestamo service.
 * It includes methods for CRUD operations and other operations related to Prestamo objects.
 */
public interface IPrestamoService {

    // CRUD methods

    /**
     * Retrieves all PrestamoDTO objects.
     *
     * @return a List of PrestamoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    List<PrestamoDTO> getAll() throws RestException;

    /**
     * Retrieves a PrestamoDTO object by its ID.
     *
     * @param id the ID of the PrestamoDTO to retrieve.
     * @return a PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    PrestamoDTO getById(Long id) throws RestException;


/**
     * Retrieves a PrestamoDTO object by its numeroPrestamo.
     *
     * @param numeroPrestamo the numeroPrestamo of the PrestamoDTO to retrieve.
     * @return a PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    PrestamoDTO getByNumeroPrestamo(Long numeroPrestamo) throws RestException;


    /**
     * Retrieves a map of Prestamo objects that are enabled, sorted by a specified attribute.
     *
     * @param page the number of the page to retrieve.
     * @param size the size of the page to retrieve.
     * @param sortby the attribute to sort the Prestamo objects by.
     * @return a Map of Prestamo objects.
     * @throws RestException if an error occurs during the operation.
     */
    Map<String, Object> prestamosEnable(int page, int size, String sortby) throws RestException;

    /**
     * Creates a new PrestamoDTO object.
     *
     * @param prestamoDTO the PrestamoDTO object to create.
     * @return the created PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    PrestamoDTO create(PrestamoDTO prestamoDTO) throws RestException;

    /**
     * Adds an Activo to a Prestamo by their IDs.
     *
     * @param id the ID of the Prestamo.
     * @param numeroInventario the ID of the Activo.
     * @throws RestException if an error occurs during the operation.
     */
    void addActivo(Long id, String numeroInventario) throws RestException;

    /**
     * Closes a Prestamo by its ID.
     *
     * @param id the ID of the Prestamo to close.
     * @throws RestException if an error occurs during the operation.
     */
    void closePrestamo(Long id) throws RestException;

}
