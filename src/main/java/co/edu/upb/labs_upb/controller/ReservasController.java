package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.ReservasAulaDTO;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.service.iface.IReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


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

    @GetMapping("/{idReserva}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getReservasById(@PathVariable Long idReserva) throws RestException {
        return ResponseEntity.ok(reservaService.getReservasById(idReserva));
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


    @PutMapping("/update/{idReserva}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateDatesReserva(@RequestBody Set<Map<String, Object>> fechas, @PathVariable Long idReserva) throws RestException {
        return ResponseEntity.ok(reservaService.updateDatesReserva(fechas, idReserva));
    }

    @DeleteMapping("/delete/{idReserva}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteReserva(@PathVariable Long idReserva) throws RestException {
        HashMap<String, Object> response = new HashMap<>();
        reservaService.deleteReserva(idReserva);

        response.put("message", "Reserva eliminada correctamente");
        return ResponseEntity.ok(response);
    }

}
