package co.edu.upb.labs_upb.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivoDTO {

    private Long id;
    private String numeroInventario;
    private String serial;
    private String modelo;
    private String descripcion;
    private String tipoActivo;
    private Long aula;
    private Long bloque;
    private Long usuario;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
