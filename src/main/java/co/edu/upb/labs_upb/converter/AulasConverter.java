package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.AulaDTO;
import co.edu.upb.labs_upb.model.Aula;
import org.springframework.stereotype.Component;


/**
 * AulasConverter is a component that provides methods to convert between Aula and AulaDTO objects.
 * It is annotated with @Component to indicate that it's a Bean and can be autowired where needed.
 */
@Component
public class AulasConverter{

    /**
     * Method to convert an Aula entity to an AulaDTO.
     *
     * @param aula the Aula entity to be converted.
     * @return the converted AulaDTO.
     */
    public AulaDTO aulaToAulaDTO(Aula aula){
        AulaDTO aulaDTO = new AulaDTO();
        aulaDTO.setId(aula.getId());
        aulaDTO.setNumero(aula.getNumero());
        aulaDTO.setDescripcion(aula.getDescripcion());
        aulaDTO.setFechaCreacion(aula.getFechaCreacion());
        aulaDTO.setFechaActualizacion(aula.getFechaActualizacion());
        return aulaDTO;
    }


    /**
     * Method to convert an AulaDTO to an Aula entity.
     *
     * @param aulaDTO the AulaDTO to be converted.
     * @return the converted Aula entity.
     */
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
