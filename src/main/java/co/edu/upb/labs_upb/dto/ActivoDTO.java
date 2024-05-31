package co.edu.upb.labs_upb.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * This class represents an Activo (Equipment) Data Transfer Object (DTO).
 * DTOs are used to transfer data between different layers of an application.
 * This specific DTO carries information about an asset in the system.
 *
 */
@Data
public class ActivoDTO {

    private Long id;
    private String numeroInventario;
    private String serial;
    private String modelo;
    private String descripcion;
    private Boolean estado;
    private String tipoActivo;
    private Long aula;
    private Long bloque;
    private Long usuario;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
