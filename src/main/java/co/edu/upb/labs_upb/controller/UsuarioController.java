package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.service.iface.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findAllUsers() throws RestException {
        List<UsuarioDTO> usuariosDto = usuarioService.findAllUsers();

        return ResponseEntity.ok().body(usuariosDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findUserById(@PathVariable Long id) throws RestException {
        UsuarioDTO usuarioDto = usuarioService.findUserById(id);

        return ResponseEntity.ok().body(usuarioDto);
    }

    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> createUser(@RequestBody UsuarioDTO usuarioDTO) throws RestException {
        UsuarioDTO usuarioDto = usuarioService.saveUser(usuarioDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDto);
    }
}
