package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.TipoActivoDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.repository.ITipoActivoRepository;
import co.edu.upb.labs_upb.service.iface.ITipoActivoService;
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
import java.util.List;

/**
 * TipoActivoController is a REST controller that handles HTTP requests related to TipoActivo objects.
 * It uses the ITipoActivoService to perform operations on TipoActivo objects.
 */
@RestController
@RequestMapping("/tipo-activo")
@CrossOrigin(origins = "*")
public class TipoActivoController {

    @Autowired
    private ITipoActivoService tipoActivoService;

    @Autowired
    private ITipoActivoRepository tipoActivoRepository;


    /**
     * Retrieves all TipoActivoDTO objects.
     *
     * @return a ResponseEntity containing a List of TipoActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> findAllTipoActivo() throws RestException {
        List<TipoActivoDTO> tipos = tipoActivoService.findAllTipoActivo();

        return ResponseEntity.ok().body(tipos);
    }

    /**
     * Retrieves a TipoActivoDTO object by its ID.
     *
     * @param id the ID of the TipoActivoDTO to retrieve.
     * @return a ResponseEntity containing a TipoActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> findTipoActivoById(@PathVariable Long id) throws RestException {
        TipoActivoDTO tipo = tipoActivoService.findTipoActivoById(id);
        return ResponseEntity.ok().body(tipo);
    }


    /**
     * Retrieves a TipoActivoDTO object by its nomenclatura.
     *
     * @param nomenclatura the nomenclatura of the TipoActivoDTO to retrieve.
     * @return a ResponseEntity containing a TipoActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @GetMapping("/nomenclatura/{nomenclatura}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> findByNomenclatura(@PathVariable String nomenclatura) throws RestException {
        TipoActivoDTO tipo = tipoActivoService.findByNomenclatura(nomenclatura);
        return ResponseEntity.ok().body(tipo);
    }

    /**
     * Creates a new TipoActivoDTO object.
     *
     * @param tipoActivo the TipoActivoDTO object to create.
     * @return a ResponseEntity containing the created TipoActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> createTipoActivo(@RequestBody TipoActivoDTO tipoActivo) throws RestException {

        // Set the creation and update dates.
        LocalDateTime now = LocalDateTime.now();
        tipoActivo.setFechaCreacion(now);
        tipoActivo.setFechaActualizacion(now);
        TipoActivoDTO tipo = tipoActivoService.saveTipoActivo(tipoActivo);
        return ResponseEntity.ok().body(tipo);
    }

    /**
     * Creates a new TipoActivoDTO object.
     *
     * @param tipoActivoDto the TipoActivoDTO object to create.
     * @return a ResponseEntity containing the created TipoActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @PutMapping("/update")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> updateTipoActivo(@RequestBody TipoActivoDTO tipoActivoDto) throws RestException {
        LocalDateTime now = LocalDateTime.now();
        TipoActivoDTO tipoEncontrado = tipoActivoService.findTipoActivoById(tipoActivoDto.getId());
        if (tipoEncontrado == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            ConstUtil.MESSAGE_NOT_FOUND,
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        // set tipoactivodto to tipoactivo foubded
        tipoEncontrado.setNomenclatura(tipoActivoDto.getNomenclatura());
        tipoEncontrado.setDescripcion(tipoActivoDto.getDescripcion());
        tipoEncontrado.setFechaActualizacion(now);

        TipoActivoDTO tipoActualizado = tipoActivoService.saveTipoActivo(tipoEncontrado);
        return ResponseEntity.ok().body(tipoActualizado);
    }

    /**
     * Deletes a TipoActivo by its ID.
     *
     * @param id the ID of the TipoActivo to delete.
     * @return a ResponseEntity containing a message.
     * @throws RestException if an error occurs during the operation.
     */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> deleteTipoActivo(@PathVariable Long id) throws RestException {
        tipoActivoService.deleteTipoActivo(id);
        return ResponseEntity.ok().body("Tipo de activo eliminado");
    }


}
