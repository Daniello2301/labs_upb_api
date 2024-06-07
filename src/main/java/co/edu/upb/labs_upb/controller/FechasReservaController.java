package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.repository.IFechasReservasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/fechas-reserva")
@CrossOrigin("*")
public class FechasReservaController {

    @Autowired
    private IFechasReservasRepository fechasReservasRepository;


    @GetMapping("/by-fecha/{fecha}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getFechasByFecha(@PathVariable String fecha) {
        return ResponseEntity.ok(fechasReservasRepository.findbyFecha(fecha));
    }

}
