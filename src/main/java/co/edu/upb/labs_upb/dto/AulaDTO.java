package co.edu.upb.labs_upb.dto;

public class AulaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private int bloque;
    private String fechaCreacion;
    private String fechaActualizacion;

    public AulaDTO() {
    }

    public AulaDTO(Long id, String nombre, String descripcion, int bloque, String fechaCreacion, String fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.bloque = bloque;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getBloque() {
        return bloque;
    }
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setBloque(int bloque) {
        this.bloque = bloque;
    }
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

}
