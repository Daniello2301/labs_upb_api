package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.converter.ActivoConverter;
import co.edu.upb.labs_upb.dto.ActivoDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Activo;
import co.edu.upb.labs_upb.model.Aula;
import co.edu.upb.labs_upb.model.Bloque;
import co.edu.upb.labs_upb.model.TipoActivo;
import co.edu.upb.labs_upb.model.Usuario;
import co.edu.upb.labs_upb.repository.IActivoRepository;
import co.edu.upb.labs_upb.repository.IAulaRepository;
import co.edu.upb.labs_upb.repository.IBloqueRepository;
import co.edu.upb.labs_upb.repository.ITipoActivoRepository;
import co.edu.upb.labs_upb.repository.IUsuarioRepository;
import co.edu.upb.labs_upb.service.iface.IActivoService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ActivoImpl is a service class that implements the IActivoService interface.
 * It provides methods for managing Activo objects, including CRUD operations and pagination.
 */
@Service
public class ActivoImpl implements IActivoService {

    @Autowired
    private IActivoRepository activoRepository;

    @Autowired
    private ActivoConverter activoConverter;

    @Autowired
    private ITipoActivoRepository tipoActivoRepository;

    @Autowired
    private IAulaRepository aulaRepository;

    @Autowired
    private IBloqueRepository bloqueRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    /**
     * Retrieves a paginated list of ActivoDTO objects.
     *
     * @param numPage  the number of the page to retrieve.
     * @param sizePage the size of the page to retrieve.
     * @return a Page of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActivoDTO> activosPagination(int numPage, int sizePage) throws RestException {

        Pageable pageable = PageRequest.of(numPage, sizePage);

        return getActivos(pageable);
    }

    /**
     * Retrieves a map of Activo objects that are enabled, sorted by a specified attribute.
     *
     * @param page   the number of the page to retrieve.
     * @param size   the size of the page to retrieve.
     * @param sortby the attribute to sort the Activo objects by.
     * @return a Map of Activo objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> activosEnable(int page, int size, String sortby) throws RestException {

        // Create a Pageable object with the given page number, size, and sorting attribute
        Pageable paging = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.ASC, sortby));

        // Retrieve the page of enabled Activo objects
        Page<Activo> activosEncontrados = activoRepository.finByEstado(true, paging);

        // If no Activo objects were found, throw a NotFoundException
        if (activosEncontrados.isEmpty()) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        // Get the content of the page
        List<Activo> response = activosEncontrados.getContent();

        // Convert the list of Activo objects to a list of ActivoDTO objects
        List<ActivoDTO> activoDTOS = response.stream()
                .map(activo -> {
                    try {
                        return toEntity(activo);
                    } catch (RestException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        // Create a map to hold the response
        Map<String, Object> activosResponse = new HashMap<>();
        activosResponse.put("Items", activoDTOS);
        activosResponse.put("currentPage", activosEncontrados.getNumber());
        activosResponse.put("totalItems", activosEncontrados.getTotalElements());
        activosResponse.put("totalPages", activosEncontrados.getTotalPages());
        activosResponse.put("pageable", activosEncontrados.getPageable());
        activosResponse.put("sort", activosEncontrados.getPageable().getSort());


        return activosResponse;
    }

    /**
     * Retrieves a paginated and sorted list of ActivoDTO objects.
     *
     * @param numPage  the number of the page to retrieve.
     * @param sizePage the size of the page to retrieve.
     * @param sortby   the attribute to sort the ActivoDTO objects by.
     * @return a Page of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActivoDTO> activosPaginationSortBy(int numPage, int sizePage, String sortby) throws RestException {

        Pageable pageable = PageRequest.of(numPage, sizePage).withSort(Sort.by(Sort.Direction.ASC, sortby));

        return getActivos(pageable);
    }


    /**
     * Retrieves all ActivoDTO objects.
     *
     * @return a List of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ActivoDTO> getAll() throws RestException {

        // Retrieve all 'Activo' entities from the database using 'activoRepository'
        List<Activo> activos = activoRepository.findAll();

        // Check if the retrieved list is empty
        if (activos.isEmpty()) {
            // Throw a 'NotFoundException' if the list is empty
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        // Convert the list of 'Activo' entities to a list of 'ActivoDTO' objects using 'activoConverter'
        // Return the list of 'ActivoDTO' objects
        return activos.stream()
                .map(activo -> {
                    try {
                        return toEntity(activo);
                    } catch (RestException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    /**
     * This method retrieves a set of `ActivoDTO` objects representing the assets associated with a specific user.
     *
     * @Override This method overrides a method from a superclass, likely named `getByUsuarioId`  with the same signature.
     * @Transactional(readOnly = true) This method is executed within a transactional context, but in read-only mode.
     *   No changes are made to the database unless explicitly committed within the transaction.
     *
     * @param numeroInventario (String) The unique identifier (likely user ID) used to search for associated assets.
     * @return Set<ActivoDTO> A set of `ActivoDTO` objects representing the retrieved assets. If no assets are found, an empty set is returned.
     * @throws RestException This method throws a `RestException` if an error occurs during data retrieval or conversion.
     *  The specific exception type can vary depending on the underlying cause.
     */
    @Override
    @Transactional(readOnly = true)
    public Set<ActivoDTO> getByUsuarioId(String numeroInventario) throws RestException {

        // Retrieve a set of 'Activo' entities associated with the specified user ID
        Set<Activo> activos = activoRepository.findByUsuarioIdUpb(numeroInventario);

        // Check if the retrieved set is empty
        if (activos.isEmpty()) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        // Convert the set of 'Activo' entities to a set of 'ActivoDTO' objects using 'toEntity'
        return activos.stream()
                .map(activo -> {
                    try {
                        return toEntity(activo);
                    } catch (RestException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves an ActivoDTO object by its ID.
     *
     * @param id the ID of the ActivoDTO to retrieve.
     * @return an ActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    @Transactional(readOnly = true)
    public ActivoDTO getById(Long id) throws RestException {

        // Find the Activo entity with the specified ID
        Activo activo = activoRepository.findById(id).orElse(null);

        // Check if the retrieved entity is null
        if (activo == null) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        return toEntity(activo);
    }

    /**
     * This method creates a new `ActivoDTO` object in the database.
     *
     * @Override This method overrides a method from a superclass, likely named `create` with the same signature.
     * @Transactional This method is executed within a transactional context.
     *
     * @param activoDto (ActivoDTO) The `ActivoDTO` object to be created.
     * @return ActivoDTO The newly created `ActivoDTO` object.
     * @throws RestException This method throws a `RestException` if an error occurs during data retrieval or conversion.
     *  The specific exception type can vary depending on the underlying cause.
     */
    @Override
    @Transactional
    public ActivoDTO create(ActivoDTO activoDto) throws RestException {

        // If the activoDto is null
        if (activoDto == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            ConstUtil.MESSAGE_NOT_FOUND,
                            HttpStatus.BAD_REQUEST.value()));
        }



        // Convert to DTO to Entity
        Activo activoNuevo = activoConverter.convertToEntity(activoDto);

        // Search and validate existence of the aula, tipoActivo, bloque and usuario
        Aula aulaEncontrada = aulaRepository.findByNumeroInTheSameBloque(activoDto.getAula(), activoDto.getBloque());
        validarBusqueda(aulaEncontrada, "aula");
        TipoActivo tipoActivoEncontrado = tipoActivoRepository.findByNomenclatura(activoDto.getTipoActivo());
        validarBusqueda(tipoActivoEncontrado, "tipoActivo");
        Bloque bloqueEncontrado = bloqueRepository.findByNumero(activoDto.getBloque());
        validarBusqueda(bloqueEncontrado, "bloque");
        Usuario usuarioEncontrado = usuarioRepository.findByIdUpb(activoDto.getUsuario());
        validarBusqueda(usuarioEncontrado, "usuario");


        // Set the tipoActivo, aula, bloque and usuario to the new activo
        activoNuevo.setTipoActivo(tipoActivoEncontrado);
        activoNuevo.setAula(aulaEncontrada);
        activoNuevo.setBloque(bloqueEncontrado);
        activoNuevo.setUsuario(usuarioEncontrado);

        // If we get the activo with the id, we update the activo
        if (activoDto.getId() != null) {
            boolean activoExiste = activoRepository.existsById(activoDto.getId());
            if (activoExiste) {
                return activoConverter.convertToDTO(activoRepository.save(activoNuevo));
            }
        }

        // if the number of inventory already exists
        Activo existeByNumInventario = activoRepository.findByNumeroInventario(activoDto.getNumeroInventario());
        if (existeByNumInventario != null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "El número de inventario ya existe",
                            HttpStatus.BAD_REQUEST.value()));
        }

        // Save the new activo
        activoRepository.save(activoNuevo);

        // Return the new activo
        activoDto.setId(activoNuevo.getId());
        return activoDto;
    }

    /**
     * This method updates an existing `ActivoDTO` object in the database.
     *
     * @Override This method overrides a method from a superclass, likely named `update` with the same signature.
     * @Transactional This method is executed within a transactional context.
     *
     * @param id (ActivoDTO) The `ActivoDTO` object to be updated.
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        // Delete the Activo entity with the specified ID
        activoRepository.deleteById(id);
    }


    /**
     * This method updates an existing `ActivoDTO` object in the database.
     *
     * @Override This method overrides a method from a superclass, likely named `update` with the same signature.
     * @Transactional This method is executed within a transactional context.
     *
     * @param activo (ActivoDTO) The `ActivoDTO` object to be updated.
     * @return ActivoDTO The updated `ActivoDTO` object.
     * @throws RestException This method throws a `RestException` if an error occurs during data retrieval or conversion.
     *  The specific exception type can vary depending on the underlying cause.
     */
    private ActivoDTO toEntity(Activo activo) throws RestException {

        // Convert the Activo entity to an ActivoDTO object
        ActivoDTO activoDTO = activoConverter.convertToDTO(activo);

        // Find the TipoActivo, Aula, Bloque, and Usuario entities associated with the Activo entity
        TipoActivo tipoActivo = tipoActivoRepository.findByNomenclatura(activo.getTipoActivo().getNomenclatura());
        validarBusqueda(tipoActivo, "tipoActivo");
        Aula aula = aulaRepository.findByNumeroInTheSameBloque(activo.getAula().getNumero(), activo.getBloque().getNumero());
        validarBusqueda(aula, "aula");
        Bloque bloque = bloqueRepository.findByNumero(activo.getBloque().getNumero());
        validarBusqueda(bloque, "bloque");
        Usuario usuario = usuarioRepository.findByIdUpb(activo.getUsuario().getIdUpb());
        validarBusqueda(usuario, "usuario");

        // Set the TipoActivo, Aula, Bloque, and Usuario attributes of the ActivoDTO object
        activoDTO.setTipoActivo(tipoActivo.getNomenclatura());
        activoDTO.setAula(aula.getNumero());
        activoDTO.setBloque(bloque.getNumero());
        activoDTO.setUsuario(usuario.getIdUpb());

        // Return the ActivoDTO object
        return activoDTO;
    }

    // Validate the search
    private void validarBusqueda(Object object, String name) throws RestException {
        // If the object is null, throw a NotFoundException
        if (object == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "Data not found with " + name,
                            HttpStatus.NOT_FOUND.value()));
        }
    }

    // Get the activos
    private Page<ActivoDTO> getActivos(Pageable pageable) throws NotFoundException {

        // Retrieve a page of Activo entities using the specified Pageable object
        Page<Activo> activos = activoRepository.findAll(pageable);

        // Check if the retrieved page is empty
        if (activos.isEmpty()) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        // Convert the page of Activo entities to a page of ActivoDTO objects using 'toEntity'
        return activos.map(activo -> {
            try {
                return toEntity(activo);
            } catch (RestException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
