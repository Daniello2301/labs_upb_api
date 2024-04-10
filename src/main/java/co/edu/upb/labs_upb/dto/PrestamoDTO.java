package co.edu.upb.labs_upb.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PrestamoDTO {

    private Long id;
    private Long numeroPrestamo;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaEntrega;
    private String laboratorio;
    private String centroCostos;
    private String facultad;
    private Boolean estado;
    private String idPersona;
    private String nombrePersona;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Set<String> activos;

}
