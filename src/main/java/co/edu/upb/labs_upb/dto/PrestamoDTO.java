package co.edu.upb.labs_upb.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * PrestamoDTO is a Data Transfer Object (DTO) class for the Prestamo entity.
 * It is annotated with @Data from Lombok to automatically generate getters, setters, equals, hashCode and toString methods.
 */
@Data
public class PrestamoDTO {

    private Long id;
    private Long numeroPrestamo;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaEntrega;
    private String laboratorio;
    private String centroCostos;
    private String facultad;

    /**
     * The current state of the Prestamo.
     * It is a Boolean where true represents that the Prestamo is active and false represents that the Prestamo is not active.
     */
    private Boolean estado;
    private String idPersona;
    private String nombrePersona;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    /**
     * The set of activos associated with the Prestamo.
     */
    private Set<String> activos;

}
