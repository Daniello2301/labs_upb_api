package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.*;
import co.edu.upb.labs_upb.model.Usuario;
import co.edu.upb.labs_upb.service.iface.IUsuarioService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/user/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findUserById(@PathVariable Long id) throws RestException {
        UsuarioDTO usuarioDto = usuarioService.findUserById(id);

        return ResponseEntity.ok().body(usuarioDto);
    }

    @PostMapping("/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> createUser(@RequestBody UsuarioDTO usuarioDTO) throws RestException {

            LocalDateTime now = LocalDateTime.now();
            usuarioDTO.setFechaCreacion(now);
            usuarioDTO.setFechaActualizacion(now);

            UsuarioDTO nuevoUsuarioDto = usuarioService.saveUser(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuarioDto);

    }


    @PutMapping("/update")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> updateUser(@RequestBody UsuarioDTO usuarioDTO) throws RestException {
        UsuarioDTO usuarioEncontrado = usuarioService.findUserById(usuarioDTO.getId());
        if (usuarioEncontrado == null) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
        LocalDateTime now = LocalDateTime.now();

        usuarioEncontrado.setDocumento(usuarioDTO.getDocumento());
        usuarioEncontrado.setNombre(usuarioDTO.getNombre());
        usuarioEncontrado.setApellido(usuarioDTO.getApellido());
        usuarioEncontrado.setCorreo(usuarioDTO.getCorreo());
        usuarioEncontrado.setPassword(usuarioDTO.getPassword());
        usuarioEncontrado.setRoles(usuarioDTO.getRoles());
        usuarioEncontrado.setFechaCreacion(usuarioEncontrado.getFechaCreacion());
        usuarioEncontrado.setFechaActualizacion(now);

        UsuarioDTO usuarioActualizado = usuarioService.saveUser(usuarioEncontrado);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioActualizado);
    }

}
