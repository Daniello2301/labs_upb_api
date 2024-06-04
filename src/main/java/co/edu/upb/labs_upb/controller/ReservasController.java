package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.ReservasAulaDTO;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.service.iface.IReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reservas")
@CrossOrigin("*")
public class ReservasController {

    @Autowired
    private IReservaService reservaService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllReservas() throws RestException {
        return ResponseEntity.ok(reservaService.getAllReservas());
    }


    @GetMapping("/aula/{idAula}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getReservasByAula(@PathVariable Long idAula) throws RestException {
        return ResponseEntity.ok(reservaService.getReservasByAula(idAula));
    }


    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createReserva(@RequestBody ReservasAulaDTO reservaDTO) throws RestException {
        return ResponseEntity.ok(reservaService.createReserva(reservaDTO));
    }

}
