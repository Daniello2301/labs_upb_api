package co.edu.upb.labs_upb.dto;

import java.time.LocalDateTime;

public class AulaDTO {

    private Long id;
    private Long numero;
    private String descripcion;
    private Long bloque;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public AulaDTO() {
    }

    public AulaDTO(Long id, Long numero, String descripcion, Long bloque, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.numero = numero;
        this.descripcion = descripcion;
        this.bloque = bloque;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public Long getNumero() {
        return numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Long getBloque() {
        return bloque;
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setBloque(Long bloque) {
        this.bloque = bloque;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

}
