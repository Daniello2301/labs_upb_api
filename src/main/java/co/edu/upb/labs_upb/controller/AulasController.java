package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.AulaDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.repository.IAulaRepository;
import co.edu.upb.labs_upb.service.iface.IAulaService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Maps;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/aulas")
@CrossOrigin(origins = "*")
public class AulasController {

    @Autowired
    private IAulaService aulaService;

    @Autowired
    private IAulaRepository aulaRepository;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findAllAulas() throws RestException {
        List<AulaDTO> aulasDto = aulaService.findAllAulas();
        return ResponseEntity.ok().body(aulasDto);
    }


    @GetMapping("/aula/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findAulaById(@PathVariable Long id) throws RestException {
        AulaDTO aulaDto = aulaService.findAulaById(id);
        return ResponseEntity.ok().body(aulaDto);
    }


    @GetMapping("/aula/numero/{numero}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findAulaByNumero(@PathVariable Long numero) throws RestException {
        AulaDTO aulaDto = aulaService.findAulaByNumero(numero);
        return ResponseEntity.ok().body(aulaDto);
    }

        @GetMapping("/aula/{aula}/bloque/{bloque}")
        @ResponseStatus(code = HttpStatus.OK)
        public ResponseEntity<Object> findAulaByNumeroInTheSameBloque(@PathVariable Long aula, @PathVariable Long bloque) throws RestException {
            List<Object> aulas = aulaRepository.findByNumeroInTheSameBloque(aula,  bloque);
            return ResponseEntity.ok().body(aulas);
        }


    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> createAula(@RequestBody AulaDTO aulaDTO) throws RestException {

        LocalDateTime now = LocalDateTime.now();
        aulaDTO.setFechaCreacion(now);
        aulaDTO.setFechaActualizacion(now);
        AulaDTO nuevaAulaDto = aulaService.saveAula(aulaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAulaDto);
    }



    @PutMapping("/update")
    public ResponseEntity<Object> updateAula(@RequestBody AulaDTO aulaDTO) throws RestException {
        LocalDateTime now = LocalDateTime.now();
        AulaDTO aulaDtoEncontrado = aulaService.findAulaById(aulaDTO.getId());
        if (aulaDtoEncontrado == null) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        aulaDtoEncontrado.setNumero(aulaDTO.getNumero());
        aulaDtoEncontrado.setDescripcion(aulaDTO.getDescripcion());
        aulaDtoEncontrado.setBloque(aulaDTO.getBloque());
        aulaDtoEncontrado.setFechaCreacion(aulaDtoEncontrado.getFechaCreacion());
        aulaDtoEncontrado.setFechaActualizacion(now);

        AulaDTO aulaDtoActualizada = aulaService.saveAula(aulaDtoEncontrado);
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaDtoActualizada);
    }


    @DeleteMapping("/delete/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteAula(@PathVariable Long id) throws RestException {
        aulaService.deleteAula(id);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Aula eliminada con éxito");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
