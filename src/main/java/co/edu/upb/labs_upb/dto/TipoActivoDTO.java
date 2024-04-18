package co.edu.upb.labs_upb.dto;

import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * TipoActivoDTO is a Data Transfer Object (DTO) class for the TipoActivo entity.
 * It is annotated with @Data from Lombok to automatically generate getters, setters, equals, hashCode and toString methods.
 */
@Data
public class TipoActivoDTO {

    private Long Id;

    private  String nomenclatura;

    private String descripcion;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    private Set<String> activos;

}
