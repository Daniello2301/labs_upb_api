package co.edu.upb.labs_upb.dto;

import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class TipoActivoDTO {

    private Long Id;

    private  String nomenclatura;

    private String descripcion;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    private Set<String> activos;

}
