package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.converter.AulasConverter;
import co.edu.upb.labs_upb.dto.AulaDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Aula;
import co.edu.upb.labs_upb.repository.IAulaRepository;
import co.edu.upb.labs_upb.repository.IBloqueRepository;
import co.edu.upb.labs_upb.service.iface.IAulaService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AulasController is a REST controller that handles HTTP requests related to Aula objects.
 * It uses the IAulaService to perform operations on Aula objects.
 */
@RestController
@RequestMapping("/aulas")
@CrossOrigin(origins = "*")
public class AulasController {

    @Autowired
    private IAulaService aulaService;

    @Autowired
    private IAulaRepository aulaRepository;

    @Autowired
    private IBloqueRepository bloqueRepository;

    @Autowired
    private AulasConverter aulasConverter;

    /**
     * Retrieves all AulaDTO objects.
     *
     * @return a ResponseEntity containing a List of AulaDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findAllAulas() throws RestException {
        List<AulaDTO> aulasDto = aulaService.findAllAulas();
        return ResponseEntity.ok().body(aulasDto);
    }


    /**
     * Retrieves an AulaDTO object by its ID.
     *
     * @param id the ID of the AulaDTO to retrieve.
     * @return a ResponseEntity containing an AulaDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/aula/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Object> findAulaById(@PathVariable Long id) throws RestException {
        AulaDTO aulaDto = aulaService.findAulaById(id);
        return ResponseEntity.ok().body(aulaDto);
    }


    /**
     * Retrieves AulaDTO objects by their number.
     *
     * @param numero the number of the AulaDTO objects to retrieve.
     * @return a ResponseEntity containing a List of AulaDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/aula/numero/{numero}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Object> findAulaByNumero(@PathVariable Long numero) throws RestException {
        List<AulaDTO> aulaDto = aulaService.findAulaByNumero(numero);
        return ResponseEntity.ok().body(aulaDto);
    }

    /**
     * Retrieves an Aula object by its number in the same block.
     *
     * @param aula the number of the Aula to retrieve.
     * @param bloque the number of the block where the Aula is located.
     * @return a ResponseEntity containing an AulaDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/aula/{aula}/bloque/{bloque}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Object> findAulaByNumeroInTheSameBloque(@PathVariable Long aula, @PathVariable Long bloque) throws RestException {

        // Get aula by number in the same block
        Aula aulaEncontrada = aulaRepository.findByNumeroInTheSameBloque(aula, bloque);
        if (aulaEncontrada == null) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()));
        }

        // convert Aula to DTO entity
        AulaDTO aulaDto = aulasConverter.aulaToAulaDTO(aulaEncontrada);

        // set block number to aulaDTO
        aulaDto.setBloque(bloqueRepository.findByNumero(aulaEncontrada.getBloque().getNumero()).getNumero());

        return ResponseEntity.ok().body(aulaDto);
    }


    /**
     * Creates a new AulaDTO object.
     *
     * @param aulaDTO the AulaDTO object to create.
     * @return a ResponseEntity containing the created AulaDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> createAula(@RequestBody AulaDTO aulaDTO) throws RestException {

        // Set creation and update dates
        LocalDateTime now = LocalDateTime.now();
        aulaDTO.setFechaCreacion(now);
        aulaDTO.setFechaActualizacion(now);
        AulaDTO nuevaAulaDto = aulaService.saveAula(aulaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAulaDto);
    }


    /**
     * Updates an AulaDTO object.
     *
     * @param aulaDTO the AulaDTO object to update.
     * @return a ResponseEntity containing the updated AulaDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @PutMapping("/update")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> updateAula(@RequestBody AulaDTO aulaDTO) throws RestException {
        // date for update
        LocalDateTime now = LocalDateTime.now();

        // get aula by id
        AulaDTO aulaDtoEncontrado = aulaService.findAulaById(aulaDTO.getId());
        if (aulaDtoEncontrado == null) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        // update aula
        aulaDtoEncontrado.setNumero(aulaDTO.getNumero());
        aulaDtoEncontrado.setDescripcion(aulaDTO.getDescripcion());
        aulaDtoEncontrado.setBloque(aulaDTO.getBloque());
        aulaDtoEncontrado.setFechaCreacion(aulaDtoEncontrado.getFechaCreacion());
        aulaDtoEncontrado.setFechaActualizacion(now);

        // save aula
        AulaDTO aulaDtoActualizada = aulaService.saveAula(aulaDtoEncontrado);
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaDtoActualizada);
    }


    /**
     * Deletes an Aula object by its ID.
     *
     * @param id the ID of the Aula object to delete.
     * @return a ResponseEntity containing a message indicating that the Aula object was deleted.
     * @throws RestException if an error occurs during the operation.
     */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> deleteAula(@PathVariable Long id) throws RestException {

        // Delete aula
        aulaService.deleteAula(id);

        // Create object response
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Aula eliminada con éxito");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
