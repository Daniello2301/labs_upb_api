package co.edu.upb.labs_upb.controller;


import co.edu.upb.labs_upb.dto.ActivoDTO;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.BadRequestException;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.InternalServerErrorException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Usuario;
import co.edu.upb.labs_upb.service.iface.IActivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * The ActivoController class is a REST controller that handles HTTP requests related to Activo objects.
 * It uses the IActivoService to perform operations on Activo objects.
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/activos")
public class ActivoController {

    @Autowired
    private IActivoService activoService;

    /**
     * Retrieves a paginated list of ActivoDTO objects.
     *
     * @param numPage the number of the page to retrieve.
     * @param sizePage the size of the page to retrieve.
     * @return a Page of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/pagination/{numPage}/{sizePage}")
    @ResponseStatus(code = HttpStatus.OK)
    public Page<ActivoDTO> activosPagination(@PathVariable int numPage, @PathVariable int sizePage) throws RestException {
        return activoService.activosPagination(numPage, sizePage);
    }


    /**
     * Retrieves a paginated and sorted list of ActivoDTO objects.
     *
     * @param numPage the number of the page to retrieve.
     * @param sizePage the size of the page to retrieve.
     * @param sortBy the attribute to sort the ActivoDTO objects by.
     * @return a Page of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/pagination/{numPage}/{sizePage}/{sortBy}")
    @ResponseStatus(code = HttpStatus.OK)
    public Page<ActivoDTO> activosPaginationSortBy(@PathVariable int numPage, @PathVariable int sizePage, @PathVariable String sortBy) throws RestException {
        return activoService.activosPaginationSortBy(numPage, sizePage, sortBy);
    }

    /**
     * Retrieves all ActivoDTO objects.
     *
     * @return a List of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Object> getAll() throws RestException {
        return ResponseEntity.ok().body(activoService.getAll());
    }

    /**
     * Retrieves a map of Activo objects that are enabled, sorted by a specified attribute.
     *
     * @param page the number of the page to retrieve.
     * @param size the size of the page to retrieve.
     * @param sortby the attribute to sort the Activo objects by.
     * @return a Map of Activo objects.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/enable/{page}/{size}/{sortby}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> getEnable(
            @PathVariable int page,
            @PathVariable int size,
            @PathVariable(name = "sortby", required = false) String sortby
    ) throws RestException {

        return ResponseEntity.ok().body(activoService.activosEnable(page, size, sortby));
    }


    /**
     * Retrieves an ActivoDTO object by its ID.
     *
     * @param id the ID of the ActivoDTO to retrieve.
     * @return a ResponseEntity containing an ActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Object> getById(@PathVariable Long id) throws RestException {
        ActivoDTO activo = activoService.getById(id);
        return ResponseEntity.ok().body(activo);
    }

    /**
     * Retrieves all ActivoDTO objects associated with a specific user.
     *
     * @param idUpb the ID of the user.
     * @return a Set of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/usuario/{idUpb}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Object> getByUsuarioId(@PathVariable String idUpb) throws RestException {
        return ResponseEntity.ok().body(activoService.getByUsuarioId(idUpb));
    }


    /**
     * Creates a new ActivoDTO object.
     *
     * @param activoDTO the ActivoDTO object to create.
     * @param authentication the authentication object.
     * @return a ResponseEntity containing the created ActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Object> create(@RequestBody ActivoDTO activoDTO, Authentication authentication) throws RestException {

        try {

            LocalDateTime now = LocalDateTime.now();

            Usuario usuarioLogueado = (Usuario) authentication.getPrincipal();

            activoDTO.setUsuario(usuarioLogueado.getIdUpb());

            activoDTO.setFechaCreacion(now);
            activoDTO.setFechaActualizacion(now);

            ActivoDTO activo = activoService.create(activoDTO);
            return ResponseEntity.ok().body(activo);

        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

    }

    /**
     * Updates an existing ActivoDTO object.
     *
     * @param activoDTO the ActivoDTO object to update.
     * @return a ResponseEntity containing the updated ActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @PutMapping("/update")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Object> update(@RequestBody ActivoDTO activoDTO) throws RestException {
        LocalDateTime now = LocalDateTime.now();
        ActivoDTO activoEncontrado = activoService.getById(activoDTO.getId());
        if (activoEncontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activo no encontrado");
        }

        activoEncontrado.setNumeroInventario(activoDTO.getNumeroInventario());
        activoEncontrado.setSerial(activoDTO.getSerial());
        activoEncontrado.setModelo(activoDTO.getModelo());
        activoEncontrado.setDescripcion(activoDTO.getDescripcion());
        activoEncontrado.setEstado(activoDTO.getEstado());
        activoEncontrado.setTipoActivo(activoDTO.getTipoActivo());
        activoEncontrado.setAula(activoDTO.getAula());
        activoEncontrado.setBloque(activoDTO.getBloque());
        activoEncontrado.setUsuario(activoDTO.getUsuario());
        activoEncontrado.setFechaCreacion(activoEncontrado.getFechaCreacion());
        activoEncontrado.setFechaActualizacion(now);


        ActivoDTO activo = activoService.create(activoEncontrado);
        return ResponseEntity.ok().body(activo);
    }

    /**
     * Deletes an ActivoDTO object by its ID.
     *
     * @param id the ID of the ActivoDTO object to delete.
     * @return a ResponseEntity containing a message indicating that the ActivoDTO object was deleted.
     * @throws RestException if an error occurs during the operation.
     */
    @PutMapping("/disable/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) throws RestException {
        LocalDateTime now = LocalDateTime.now();
        ActivoDTO response = activoService.getById(id);
        if (response == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "Activo no encontrado",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        } else {

            response.setFechaActualizacion(now);
            response.setEstado(false);
            activoService.create(response);

            Map<String, Object> responseMap = new HashMap<>();

            responseMap.put("Message: ", "Activo desactivado correctamente");
            responseMap.put("Activo: ", response);
            return ResponseEntity.ok().body(responseMap);

        }
    }

}
