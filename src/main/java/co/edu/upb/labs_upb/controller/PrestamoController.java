package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.PrestamoDTO;
import co.edu.upb.labs_upb.exception.*;
import co.edu.upb.labs_upb.model.Activo;
import co.edu.upb.labs_upb.model.Prestamo;
import co.edu.upb.labs_upb.repository.IActivoRepository;
import co.edu.upb.labs_upb.repository.IPrestamoRepository;
import co.edu.upb.labs_upb.service.iface.IActivoService;
import co.edu.upb.labs_upb.service.iface.IPrestamoService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;


/**
 * PrestamoController is a REST controller that handles HTTP requests related to Prestamo objects.
 * It uses the IPrestamoService to perform operations on Prestamo objects.
 */
@RestController
@RequestMapping("/prestamos")
@CrossOrigin("*")
public class PrestamoController {

    @Autowired
    private IPrestamoService prestamoService;

    /**
     * Retrieves all PrestamoDTO objects.
     *
     * @return a ResponseEntity containing a List of PrestamoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAll() throws RestException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(prestamoService.getAll());
        } catch (InternalServerErrorException ex){
            throw ex;
        }catch (Exception ex){
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ex.getMessage(),
                    HttpStatus.NOT_FOUND.value()));
        }
    }


    /**
     * Retrieves a page of Prestamo objects that are enabled, sorted by a specified attribute.
     *
     * @param numPage the number of the page to retrieve.
     * @param sizePage the size of the page to retrieve.
     * @param sortby the attribute to sort the Prestamo objects by.
     * @return a ResponseEntity containing a page of Prestamo objects.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/pagination/{numPage}/{sizePage}/{sortby}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> prestamosPagination(@PathVariable int numPage, @PathVariable int sizePage, @PathVariable String sortby) throws RestException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(prestamoService.prestamosEnable(numPage, sizePage, sortby));
        } catch (InternalServerErrorException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    /**
     * Retrieves a PrestamoDTO object by its id.
     *
     * @param id the id of the PrestamoDTO object to retrieve.
     * @return a ResponseEntity containing the PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/prestamo/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getById(@PathVariable Long id) throws RestException {
        try {
            PrestamoDTO prestamoDTO = prestamoService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(prestamoDTO);
        } catch (NotFoundException ex){
            throw ex;
        }catch (Exception ex){
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ex.getMessage(),
                    HttpStatus.NOT_FOUND.value()));
        }
    }


    /**
     * Retrieves a PrestamoDTO object by its numeroPrestamo.
     *
     * @param numeroPrestamo the numeroPrestamo of the PrestamoDTO object to retrieve.
     * @return a ResponseEntity containing the PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/numero-prestamo/{numeroPrestamo}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getByNumeroPrestamo(@PathVariable Long numeroPrestamo) throws RestException {
        try {
            PrestamoDTO prestamoDTO = prestamoService.getByNumeroPrestamo(numeroPrestamo);
            return ResponseEntity.status(HttpStatus.OK).body(prestamoDTO);
        } catch (NotFoundException ex){
            throw ex;
        }catch (Exception ex){
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ex.getMessage(),
                    HttpStatus.NOT_FOUND.value()));
        }
    }



    /**
     * Creates a new PrestamoDTO object.
     *
     * @param prestamoDTO the PrestamoDTO object to create.
     * @return a ResponseEntity containing the created PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody PrestamoDTO prestamoDTO) throws RestException {
        try {
            LocalDateTime now = LocalDateTime.now();
            prestamoDTO.setFechaCreacion(now);
            prestamoDTO.setFechaActualizacion(now);

            PrestamoDTO prestamoGuardado = prestamoService.create(prestamoDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(prestamoGuardado);

        } catch (BadRequestException ex){
            throw ex;
        }catch (Exception ex){
            throw new BadRequestException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST.value()));
        }

    }


    /**
     * Updates a PrestamoDTO object.
     *
     * @param prestamoDTO the PrestamoDTO object to update.
     * @return a ResponseEntity containing the updated PrestamoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @PutMapping("/update")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> update(@RequestBody PrestamoDTO prestamoDTO) throws RestException {
        try {

            // Date for update
            LocalDateTime now = LocalDateTime.now();
            prestamoDTO.setFechaActualizacion(now);

            // Find the prestamo to update
            PrestamoDTO prestamoEncontrado = prestamoService.getById(prestamoDTO.getId());
            if (prestamoEncontrado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prestamo no encontrado");
            }


            // setting de body request to prestamo founded
            prestamoEncontrado.setNumeroPrestamo(prestamoEncontrado.getNumeroPrestamo());
            prestamoEncontrado.setFechaSalida(prestamoEncontrado.getFechaSalida());
            prestamoEncontrado.setFechaEntrega(prestamoDTO.getFechaEntrega());
            prestamoEncontrado.setLaboratorio(prestamoDTO.getLaboratorio());
            prestamoEncontrado.setCentroCostos(prestamoDTO.getCentroCostos());
            prestamoEncontrado.setFacultad(prestamoDTO.getFacultad());
            prestamoEncontrado.setEstado(prestamoDTO.getEstado());
            prestamoEncontrado.setIdPersona(prestamoDTO.getIdPersona());
            prestamoEncontrado.setNombrePersona(prestamoDTO.getNombrePersona());
            prestamoEncontrado.setFechaCreacion(prestamoEncontrado.getFechaCreacion());
            prestamoEncontrado.setFechaActualizacion(now);

            return ResponseEntity.status(HttpStatus.OK).body(prestamoService.create(prestamoEncontrado));

        } catch (BadRequestException ex){
            throw ex;
        }catch (Exception ex){
            throw new BadRequestException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST.value()));
        }

    }


    /**
     * Deletes a PrestamoDTO object by its id.
     *
     * @param numeroInventario the activo that will be added to the prestamo.
     * @param idPrestamo the prestamo id for add new activo.
     * @return a ResponseEntity containing a message indicating the result of the operation.
     * @throws RestException if an error occurs during the operation.
     */
    @PutMapping("/add_activo/{idPrestamo}/{numeroInventario}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Object> addActivo(@PathVariable Long idPrestamo, @PathVariable String numeroInventario) throws RestException {

        try {
            // validate de params
            if (idPrestamo == null || numeroInventario == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El id del prestamo y el id del activo son requeridos");
            }

            // add the activo to prestamo
            prestamoService.addActivo(idPrestamo, numeroInventario);

            // create response
            HashMap<String, String> response = new HashMap<>();

            response.put("message", "Activo agregado al prestamo");
            response.put("Activo agregado: ", numeroInventario);
            response.put("Prestamo modificado: ", idPrestamo.toString());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (BadRequestException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    /**
     * Closes a Prestamo by its ID.
     *
     * @param id the ID of the Prestamo to close.
     * @return a ResponseEntity containing a message.
     * @throws RestException if an error occurs during the operation.
     */
    @PutMapping("/close/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> closePrestamo(@PathVariable Long id) throws RestException {
        try {

            PrestamoDTO prestamoDTO = prestamoService.getById(id);
            if (prestamoDTO == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prestamo no encontrado");
            }

            prestamoService.closePrestamo(prestamoDTO.getId());

            HashMap<String, String> response = new HashMap<>();

            response.put("message", "Prestamo cerrado correctamente");
            response.put("Prestamo cerrado: ", prestamoDTO.getNumeroPrestamo().toString());

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (BadRequestException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    }

}
