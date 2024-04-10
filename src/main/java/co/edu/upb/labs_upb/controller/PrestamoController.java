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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/prestamos")
@CrossOrigin("*")
public class PrestamoController {

    @Autowired
    private IPrestamoService prestamoService;

    @Autowired
    private IPrestamoRepository prestamoRepository;

    @Autowired
    private IActivoRepository activoRepository;

    @Autowired
    private IActivoService activoService;


    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
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


    @GetMapping("/pagination/{numPage}/{sizePage}/{sortby}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> prestamosPagination(@PathVariable int numPage, @PathVariable int sizePage, @PathVariable String sortby) throws RestException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(prestamoService.prestamosEnable(numPage, sizePage, sortby));
        } catch (InternalServerErrorException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    @GetMapping("/prestamo/{id}")
    @ResponseStatus(code = HttpStatus.OK)
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


    @PutMapping("/update")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> update(@RequestBody PrestamoDTO prestamoDTO) throws RestException {
        try {
            LocalDateTime now = LocalDateTime.now();
            prestamoDTO.setFechaActualizacion(now);

            PrestamoDTO prestamoEncontrado = prestamoService.getById(prestamoDTO.getId());
            if (prestamoEncontrado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prestamo no encontrado");
            }

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


    @PutMapping("/add_activo/{idPrestamo}/{numeroInventario}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> addActivo(@PathVariable Long idPrestamo, @PathVariable String numeroInventario) throws RestException {

        try {

            if (idPrestamo == null || numeroInventario == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El id del prestamo y el id del activo son requeridos");
            }

            prestamoService.addActivo(idPrestamo, numeroInventario);

            HashMap<String, String> response = new HashMap<>();

            response.put("message", "Activo agregado al prestamo");
            response.put("Activo agregado: ", numeroInventario);
            response.put("Prestamo modificado: ", idPrestamo.toString());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (BadRequestException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


    @PutMapping("/close/{id}")
    @ResponseStatus(code = HttpStatus.OK)
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
