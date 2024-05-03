package co.edu.upb.labs_upb.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;


/**
 * TipoActivoDTO is a Data Transfer Object (DTO) class for the TipoActivo entity.
 * It is annotated with @Data from Lombok to automatically generate getters, setters, equals, hashCode and toString methods.
 */
@Data
public class UsuarioDTO {

    private Long id;

    private Long idUpb;
    private Long documento;

    private String nombre;

    private String apellido;
    private String email;

    private String password;

    private Set<String> roles;
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;
}
