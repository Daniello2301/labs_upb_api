package co.edu.upb.labs_upb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservasAulaDTO {

    private Long id;
    private Long aula;
    private Long bloque;
    private String persona;
    private String descripcion;
    private Boolean estado;
    private Set<Map<String, Object>> fechasReserva;
}
