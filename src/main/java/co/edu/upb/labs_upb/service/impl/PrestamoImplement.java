package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.converter.PrestamoConverter;
import co.edu.upb.labs_upb.dto.PrestamoDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.InternalServerErrorException;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Activo;
import co.edu.upb.labs_upb.model.Prestamo;
import co.edu.upb.labs_upb.repository.IActivoRepository;
import co.edu.upb.labs_upb.repository.IPrestamoRepository;
import co.edu.upb.labs_upb.service.iface.IPrestamoService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * PrestamoImplement is a service class that implements the IPrestamoService interface.
 * It provides methods for managing Prestamo objects, including CRUD operations and other operations.
 */
@Service
public class PrestamoImplement implements IPrestamoService {


    @Autowired
    private IPrestamoRepository prestamoRepository;

    @Autowired
    private IActivoRepository activoRepository;

    @Autowired
    private PrestamoConverter prestamoConverter;

    /**
     * Retrieves all PrestamoDTO objects.
     *
     * @return a List of PrestamoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PrestamoDTO> getAll() throws RestException {

        // Retrieve all Prestamo objects from the database.
        List<Prestamo> prestamos = prestamoRepository.findAll();

        // If no Prestamo objects are found, throw an exception.
        if (prestamos.isEmpty()) {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_ERROR_DATA,
                    HttpStatus.NOT_FOUND.value()));
        }

        // Create a list to store the PrestamoDTO objects.
        List<PrestamoDTO> prestamosDTO = new ArrayList<>();

        // Convert each Prestamo object to a PrestamoDTO object and add it to the list.
        for (Prestamo prestamo : prestamos) {
            PrestamoDTO prestamoDTO = prestamoConverter.entityToDto(prestamo);

            // Create a set to store the activos of the Prestamo object.
            Set<String> activos = new HashSet<>();

            // For each prestamo, we get the activos.
            for (Activo activo : prestamo.getActivos()) {
                // add the numero Inventario of the activo to the setActivos.
                activos.add(activo.getNumeroInventario());
            }

            // Set activos to prestamosDTO list.
            prestamoDTO.setActivos(activos);

            // add prestamoDTO to prestamosDTO list.
            prestamosDTO.add(prestamoDTO);
        }


        return prestamosDTO;
    }

    /**
     * Retrieves a PrestamoDTO object by its ID.
     *
     * @param id the ID of the PrestamoDTO to retrieve.
     * @return a PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    @Transactional(readOnly = true)
    public PrestamoDTO getById(Long id) throws RestException {
        if (id  == null) {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ConstUtil.MESSAGE_ERROR_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        // Find by id the prestamo object.
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
        if (prestamo == null) {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El prestamo con el id " + id + " no existe",
                    HttpStatus.NOT_FOUND.value()));
        }

        // convert to DTO the prestamo object.
        PrestamoDTO prestamoDTO = prestamoConverter.entityToDto(prestamo);

        // get activos by prestamo.
        Set<Activo> activosByPrestamo = activoRepository.findByIdPrestamo(prestamo.getId());

        // create a set for activos.
        Set<String> activos = new HashSet<>();

        // Foreach activo get numeroInventario and add to activos set.
        for (Activo activo : activosByPrestamo) {
            activos.add(activo.getNumeroInventario());
        }

        // set activos to prestamoDTO.
        prestamoDTO.setActivos(activos);

        return prestamoDTO;
    }

    /**
     * Retrieves a PrestamoDTO object by its numeroPrestamo.
     *
     * @param numeroPrestamo the numeroPrestamo of the PrestamoDTO to retrieve.
     * @return a PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public PrestamoDTO getByNumeroPrestamo(Long numeroPrestamo) throws RestException {
        if (numeroPrestamo  == null) {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ConstUtil.MESSAGE_ERROR_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        // Find by id the prestamo object.
        Prestamo prestamoFounded = prestamoRepository.findByNumeroPrestamo(numeroPrestamo);
        if (prestamoFounded == null) {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El prestamo con el id " + numeroPrestamo + " no existe",
                    HttpStatus.NOT_FOUND.value()));
        }

        // convert to DTO the prestamo object.
        PrestamoDTO prestamoDTO = prestamoConverter.entityToDto(prestamoFounded);

        // get activos by prestamo.
        Set<Activo> activosByPrestamo = activoRepository.findByIdPrestamo(prestamoFounded.getId());

        // create a set for activos.
        Set<String> activos = new HashSet<>();

        // Foreach activo get numeroInventario and add to activos set.
        for (Activo activo : activosByPrestamo) {
            activos.add(activo.getNumeroInventario());
        }

        // set activos to prestamoDTO.
        prestamoDTO.setActivos(activos);

        return prestamoDTO;
    }

    /**
     * Retrieves a map of Prestamo objects that are enabled, sorted by a specified attribute.
     *
     * @param page the number of the page to retrieve.
     * @param size the size of the page to retrieve.
     * @param sortby the attribute to sort the Prestamo objects by.
     * @return a Map of Prestamo objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public Map<String, Object> prestamosEnable(int page, int size, String sortby) throws RestException {

        // Create a Pageable object to store the paging information.
        Pageable paging = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.ASC, sortby));

        // Find all Prestamo objects that are enabled.
        Page<Prestamo> prestamoEncontrados = prestamoRepository.finByEstado(true, paging);
        if (prestamoEncontrados.isEmpty()) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_ERROR_DATA, HttpStatus.NOT_FOUND.value()));
        }

        //get content from page prestamo response.
        List<Prestamo> prestamosResponse = prestamoEncontrados.getContent();


        // convert to DTO the prestamo objects.
        List<PrestamoDTO> prestamosDTO = prestamosResponse.stream()
                .map(prestamo -> prestamoConverter.entityToDto(prestamo))
                .collect(Collectors.toList());


        // create map response
        Map<String, Object> response = new HashMap<>();
        response.put("Items", prestamosDTO);
        response.put("currentPage", prestamoEncontrados.getNumber());
        response.put("totalItems", prestamoEncontrados.getTotalElements());
        response.put("totalPages", prestamoEncontrados.getTotalPages());
        response.put("pageable", prestamoEncontrados.getPageable());
        response.put("sort", prestamoEncontrados.getPageable().getSort());


        return response;
    }

    /**
     * Creates a new PrestamoDTO object.
     *
     * @param prestamoDTO the PrestamoDTO object to create.
     * @return the created PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public PrestamoDTO create(PrestamoDTO prestamoDTO) throws RestException {

        if (prestamoDTO == null) {
             throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                     ConstUtil.MESSAGE_ERROR_DATA,
                     HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        // convert to entity the prestamoDTO object.
        Prestamo prestamoConvertido = prestamoConverter.dtoToEntity(prestamoDTO);

        // if prestamoDTO has an id, we check if the prestamo exists, and update;
        if (prestamoDTO.getId() != null) {
            boolean exist = prestamoRepository.existsById(prestamoDTO.getId());
            if (exist) {
                return prestamoConverter.entityToDto(prestamoRepository.save(prestamoConvertido));
            }
        }

        // save the prestamo object.
        Prestamo prestamoNuevo = prestamoRepository.save(prestamoConvertido);

        // for each activo in prestamoDTO, we check if the activo exists, and update;
        for (String activo : prestamoDTO.getActivos()) {
            if (activo != null) {
                Activo activoEncontrado = activoRepository.findByNumeroInventario(activo);
                if (activoEncontrado == null) {
                    throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "El activo con el numero de inventario " + activo + " no existe",
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
                }

                // update prestamo in activo.
                activoEncontrado.setPrestamo(prestamoNuevo);

                // save activo.
                activoRepository.updatePrestamo(prestamoNuevo.getId(), activoEncontrado.getNumeroInventario());
            }
        }


        prestamoDTO.setId(prestamoNuevo.getId());

        return prestamoDTO;
    }


    /**
     * Updates a PrestamoDTO object.
     *
     * @param numeroInventario the PrestamoDTO object to update.
     * @param id the ID of the PrestamoDTO object to update.
     * @return the updated PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public void addActivo(Long id, String numeroInventario) throws RestException {

        // find prestamo by id.
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
        if (prestamo == null) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El prestamo con el id " + id + " no existe",
                    HttpStatus.NOT_FOUND.value()));
        }

        // check if prestamo is closed.
        if (!prestamo.getEstado()) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El prestamo con el id " + id + " ya fue cerrado",
                    HttpStatus.NOT_FOUND.value()));
        }

        // find activo by numeroInventario.
        Activo activo = activoRepository.findByNumeroInventario(numeroInventario);
        if (activo == null) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El activo con el numero de inventario " + numeroInventario + " no existe",
                    HttpStatus.NOT_FOUND.value()));
        }

        // update prestamo in activo.
        activoRepository.updatePrestamo(prestamo.getId(), activo.getNumeroInventario());
    }

    /**
     * Closes a Prestamo by its ID.
     *
     * @param id the ID of the Prestamo to close.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public void closePrestamo(Long id) throws RestException {

        // find prestamo by id.
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
        if (prestamo == null) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El prestamo con el id " + id + " no existe",
                    HttpStatus.NOT_FOUND.value()));
        }

        // set fechaEntrega to prestamo.
        prestamo.setFechaEntrega(LocalDateTime.now());

        // close prestamo
        prestamoRepository.closePrestamo(prestamo.getId());

        // get activos from prestamo.
        Set<Activo> activosFromPrestamo = activoRepository.findByIdPrestamo(prestamo.getId());

        // for each activo in prestamo, we update prestamo to null.
        for (Activo activo : activosFromPrestamo) {
            Activo activoEncontrado = activoRepository.findByNumeroInventario(activo.getNumeroInventario());
            if (activoEncontrado == null) {
                throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "El activo con el numero de inventario " + activo.getNumeroInventario() + " no existe",
                        HttpStatus.NOT_FOUND.value()));
            }
            activoRepository.updatePrestamo(null, activo.getNumeroInventario());
        }
    }
}
