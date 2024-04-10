package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.ActivoDTO;
import co.edu.upb.labs_upb.exception.*;
import co.edu.upb.labs_upb.model.Activo;
import co.edu.upb.labs_upb.service.iface.IActivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/activos")
public class ActivoController {

    @Autowired
    private IActivoService activoService;

    @GetMapping("/pagination/{numPage}/{sizePage}")
    @ResponseStatus(code = HttpStatus.OK)
    public Page<ActivoDTO> activosPagination(@PathVariable int numPage, @PathVariable int sizePage) throws RestException {
        return activoService.activosPagination(numPage, sizePage);
    }


    @GetMapping("/pagination/{numPage}/{sizePage}/{sortBy}")
    @ResponseStatus(code = HttpStatus.OK)
    public Page<ActivoDTO> activosPaginationSortBy(@PathVariable int numPage, @PathVariable int sizePage, @PathVariable String sortBy) throws RestException {
        return activoService.activosPaginationSortBy(numPage, sizePage, sortBy);
    }

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getAll() throws RestException {
        return ResponseEntity.ok().body(activoService.getAll());
    }

    @GetMapping("/enable/{page}/{size}/{sortby}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> getEnable(
            @PathVariable int page,
            @PathVariable int size,
            @PathVariable String sortby
    ) throws RestException {
        return ResponseEntity.ok().body(activoService.activosEnable(page, size, sortby));
    }

    @GetMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getById(@PathVariable Long id) throws RestException {
        ActivoDTO activo = activoService.getById(id);
        return ResponseEntity.ok().body(activo);
    }

    @GetMapping("/usuario/{idUpb}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getByUsuarioId(@PathVariable String idUpb) throws RestException {
        return ResponseEntity.ok().body(activoService.getByUsuarioId(idUpb));
    }


    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody ActivoDTO activoDTO) throws RestException {

        try {

            LocalDateTime now = LocalDateTime.now();

            activoDTO.setFechaCreacion(now);
            activoDTO.setFechaActualizacion(now);

            ActivoDTO activo = activoService.create(activoDTO);
            return ResponseEntity.ok().body(activo);

        }catch (BadRequestException ex){
            throw ex;
        }catch (Exception ex){
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

    }

    @PutMapping("/update")
    @ResponseStatus(code = HttpStatus.CREATED)
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


    @PutMapping("/disable/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) throws RestException {
        LocalDateTime now = LocalDateTime.now();
        ActivoDTO response = activoService.getById(id);
        if (response == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "Activo no encontrado",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }else{

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
