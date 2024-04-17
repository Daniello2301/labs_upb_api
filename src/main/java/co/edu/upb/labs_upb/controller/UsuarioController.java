package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.ChangePasswordReset;
import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.*;
import co.edu.upb.labs_upb.model.Usuario;
import co.edu.upb.labs_upb.service.iface.IUsuarioService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        if(!authentication.isAuthenticated())
        {
            throw new RestException(ErrorDto.getErrorDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_AUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value()));
        }

        UsuarioDTO usuarioDTO = usuarioService.findByCorreo(authentication.getName());

        usuarioDTO.setPassword(null);
        System.out.println(authentication.getPrincipal());

        /*
        HashMap<String, Object> response = new HashMap<>();
        response.put("ID: ",authentication.getPrincipal());
        * "id": 203,
    "idUpb": 3001254,
    "documento": 7012543,
    "nombre": "ADMIN",
    "enable": true,
    "apellido": "Admin",
    "email": "admin@mail.com",
    "password": "$2a$10$97O1DYHx5.L.fVo4bNQP6um1N0/dNs9yeYSqHVzGeC97v2QWNkIjy",
    "roles": [
        {
            "id": 1,
            "nombre": "ADMIN",
            "descripcion": "Rol de administracion",
            "fechaCreacion": "2024-02-23T14:33:37",
            "fechaActualizacion": "2024-02-23T14:33:37"
        }
    ],
    "fechaCreacion": "2024-04-16T14:39:15.198627",
    "fechaActualizacion": null,
    "enabled": true,
    "username": "admin@mail.com",
    "authorities": [
        {
            "authority": "ADMIN"
        }
    ],
    "accountNonLocked": true,
    "credentialsNonExpired": true,
    "accountNonExpired": true
        * */

        return ResponseEntity.ok().body(usuarioDTO);
    }


    @PutMapping("/reset-password")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Object> resetPassword(@RequestBody ChangePasswordReset changePasswordReset, Authentication connectedUser) throws RestException {

        try{
            if(!connectedUser.isAuthenticated())
            {
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
        }catch (BadRequestException ex){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
