package co.edu.upb.labs_upb.dto;

import co.edu.upb.labs_upb.model.Rol;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class UsuarioDTO {

    private Long id;

    private Long idUpb;
    private Long documento;

    private String nombre;

    private String apellido;
    private String correo;

    private String password;

    private Set<String> roles;
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;
}
