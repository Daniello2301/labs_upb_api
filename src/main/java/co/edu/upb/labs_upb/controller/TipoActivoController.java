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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tipo-activo")
@CrossOrigin(origins = "*")
public class TipoActivoController {

    @Autowired
    private ITipoActivoService tipoActivoService;

    @Autowired
    private ITipoActivoRepository tipoActivoRepository;


    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findAllTipoActivo() throws RestException {
        List<TipoActivoDTO> tipos = tipoActivoService.findAllTipoActivo();

        return ResponseEntity.ok().body(tipos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findTipoActivoById(@PathVariable Long id) throws RestException {
        TipoActivoDTO tipo = tipoActivoService.findTipoActivoById(id);
        return ResponseEntity.ok().body(tipo);
    }


    @GetMapping("/nomenclatura/{nomenclatura}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findByNomenclatura(@PathVariable String nomenclatura) throws RestException {
        TipoActivoDTO tipo = tipoActivoService.findByNomenclatura(nomenclatura);
        return ResponseEntity.ok().body(tipo);
    }

    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> createTipoActivo(@RequestBody TipoActivoDTO tipoActivo) throws RestException {

        LocalDateTime now = LocalDateTime.now();
        tipoActivo.setFechaCreacion(now);
        tipoActivo.setFechaActualizacion(now);
        TipoActivoDTO tipo = tipoActivoService.saveTipoActivo(tipoActivo);
        return ResponseEntity.ok().body(tipo);
    }


    @PutMapping("/update")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> updateTipoActivo(@RequestBody TipoActivoDTO tipoActivoDto) throws RestException {
        LocalDateTime now = LocalDateTime.now();
        TipoActivoDTO tipoEncontrado = tipoActivoService.findTipoActivoById(tipoActivoDto.getId());
        if(tipoEncontrado == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            ConstUtil.MESSAGE_NOT_FOUND,
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        tipoEncontrado.setNomenclatura(tipoActivoDto.getNomenclatura());
        tipoEncontrado.setDescripcion(tipoActivoDto.getDescripcion());
        tipoEncontrado.setFechaActualizacion(now);

        TipoActivoDTO tipoActualizado = tipoActivoService.saveTipoActivo(tipoEncontrado);
        return ResponseEntity.ok().body(tipoActualizado);
    }


    @DeleteMapping("/delete/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> deleteTipoActivo(@PathVariable Long id) throws RestException {
        tipoActivoService.deleteTipoActivo(id);
        return ResponseEntity.ok().body("Tipo de activo eliminado");
    }


}
