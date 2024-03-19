package co.edu.upb.labs_upb.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PrestamoDTO {

    private Long id;
    private String fechaSalido;
    private String fechaEntrega;
    private String laboratorio;
    private String centroCostos;
    private String facultad;
    private String idPersona;
    private String nombrePersona;
    private String fechaCreacion;
    private String fechaActualizacion;
    private Set<String> activos;

}
