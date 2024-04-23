package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.ChangePasswordReset;
import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.BadRequestException;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.service.iface.IUsuarioService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;


    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Object> findAllUsers() throws RestException {
        List<UsuarioDTO> usuariosDto = usuarioService.findAllUsers();

        return ResponseEntity.ok().body(usuariosDto);
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Object> findUserById(@PathVariable Long id) throws RestException {
        UsuarioDTO usuarioDto = usuarioService.findUserById(id);

        return ResponseEntity.ok().body(usuarioDto);
    }


    @PutMapping("/update")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> updateUser(@RequestBody UsuarioDTO usuarioDTO) throws RestException {
        UsuarioDTO usuarioEncontrado = usuarioService.findUserById(usuarioDTO.getId());
        if (usuarioEncontrado == null) {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
        LocalDateTime now = LocalDateTime.now();

        usuarioEncontrado.setNombre(usuarioDTO.getNombre());
        usuarioEncontrado.setApellido(usuarioDTO.getApellido());
        usuarioEncontrado.setEmail(usuarioDTO.getEmail());
        usuarioEncontrado.setRoles(usuarioDTO.getRoles());
        usuarioEncontrado.setFechaActualizacion(now);
        usuarioEncontrado.setPassword(usuarioDTO.getPassword());

        UsuarioDTO usuarioActualizado = usuarioService.saveUser(usuarioEncontrado);

        HashMap<String, Object> response = new HashMap<>();

        response.put("message", "Usuario actualizado correctamente");
        response.put("ID", usuarioActualizado.getIdUpb());
        response.put("Documento", usuarioActualizado.getDocumento());
        response.put("Nombre", usuarioActualizado.getNombre());
        response.put("Email", usuarioActualizado.getEmail());
        response.put("Password", usuarioActualizado.getPassword());
        response.put("Roles", usuarioActualizado.getRoles());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }



    @GetMapping("/user")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Object> userInfo(Authentication authentication) throws RestException {
        if (!authentication.isAuthenticated()) {
            throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_AUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value()));
        }

        UsuarioDTO usuarioDTO = usuarioService.findByCorreo(authentication.getName());

        usuarioDTO.setPassword(null);
        System.out.println(authentication.getPrincipal());

        return ResponseEntity.ok().body(usuarioDTO);
    }


    @PutMapping("/reset-password")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Object> resetPassword(@RequestBody ChangePasswordReset changePasswordReset, Authentication connectedUser) throws RestException {

        try {
            if (!connectedUser.isAuthenticated()) {
                throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ConstUtil.MESSAGE_NOT_AUTHORIZED,
                        HttpStatus.UNAUTHORIZED.value()));
            }

            UsuarioDTO usuarioEncontrado = usuarioService.findByCorreo(connectedUser.getName());
            if (usuarioEncontrado == null) {
                throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        ConstUtil.MESSAGE_NOT_FOUND,
                        HttpStatus.INTERNAL_SERVER_ERROR.value()));
            }

            usuarioService.changePassword(changePasswordReset, connectedUser);

            HashMap<String, Object> response = new HashMap<>();
            response.put("Message", "Contraseña actualizada correctamente");


            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (BadRequestException ex) {

           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

        }

    }


}

