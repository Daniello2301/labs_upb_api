package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.AulaDTO;
import co.edu.upb.labs_upb.model.Aula;
import org.springframework.stereotype.Component;

@Component
public class AulasConverter{

    // Convert Aula Model to AulaDTO
    public AulaDTO aulaToAulaDTO(Aula aula){
        AulaDTO aulaDTO = new AulaDTO();
        aulaDTO.setId(aula.getId());
        aulaDTO.setNumero(aula.getNumero());
        aulaDTO.setDescripcion(aula.getDescripcion());
        aulaDTO.setFechaCreacion(aula.getFechaCreacion());
        aulaDTO.setFechaActualizacion(aula.getFechaActualizacion());
        return aulaDTO;
    }


    // Convert AulaDTO to Aula Model

    public Aula aulaDTOToAula(AulaDTO aulaDTO){
        Aula aula = new Aula();
        aula.setId(aulaDTO.getId());
        aula.setNumero(aulaDTO.getNumero());
        aula.setDescripcion(aulaDTO.getDescripcion());
        aula.setFechaCreacion(aulaDTO.getFechaCreacion());
        aula.setFechaActualizacion(aulaDTO.getFechaActualizacion());
        return aula;
    }

}
