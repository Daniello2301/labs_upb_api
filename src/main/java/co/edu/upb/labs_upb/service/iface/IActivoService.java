package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.ActivoDTO;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Activo;
import co.edu.upb.labs_upb.model.Usuario;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * IActivoService is an interface that defines the contract for the Activo service.
 * It includes methods for pagination, sorting, and CRUD operations.
 */
public interface IActivoService {

    /**
     * Retrieves a paginated list of ActivoDTO objects.
     *
     * @param numPage the number of the page to retrieve.
     * @param sizePage the size of the page to retrieve.
     * @return a Page of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    Page<ActivoDTO> activosPagination(int numPage, int sizePage) throws RestException;

    /**
     * Retrieves a map of Activo objects that are enabled, sorted by a specified attribute.
     *
     * @param page the number of the page to retrieve.
     * @param size the size of the page to retrieve.
     * @param sortby the attribute to sort the Activo objects by.
     * @return a Map of Activo objects.
     * @throws RestException if an error occurs during the operation.
     */
    Map<String, Object> activosEnable(int page, int size, String sortby) throws RestException;

    /**
     * Retrieves a paginated and sorted list of ActivoDTO objects.
     *
     * @param numPage the number of the page to retrieve.
     * @param sizePage the size of the page to retrieve.
     * @param sortBy the attribute to sort the ActivoDTO objects by.
     * @return a Page of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    Page<ActivoDTO> activosPaginationSortBy(int numPage, int sizePage, String sortBy) throws RestException;


    /**
     * Retrieves all ActivoDTO objects.
     *
     * @return a List of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    List<ActivoDTO> getAll() throws RestException;

    /**
     * Retrieves all ActivoDTO objects associated with a specific user.
     *
     * @param idUsuarioUpb the ID of the user.
     * @return a Set of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    Set<ActivoDTO>  getByUsuarioId(String idUsuarioUpb) throws RestException;

    /**
     * Retrieves an ActivoDTO object by its ID.
     *
     * @param id the ID of the ActivoDTO to retrieve.
     * @return an ActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    ActivoDTO getById(Long id) throws RestException;

    /**
     * Creates a new ActivoDTO object.
     *
     * @param activoDTO the ActivoDTO object to create.
     * @return the created ActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    ActivoDTO create(ActivoDTO activoDTO) throws RestException;

    /**
     * Deletes an ActivoDTO object by its ID.
     *
     * @param id the ID of the ActivoDTO to delete.
     */
    void deleteById(Long id) ;

}
