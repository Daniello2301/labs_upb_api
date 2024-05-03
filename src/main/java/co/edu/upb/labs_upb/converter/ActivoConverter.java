package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.ActivoDTO;
import co.edu.upb.labs_upb.model.Activo;
import org.springframework.stereotype.Component;

/**
 * ActivoConverter is a component that provides methods to convert between Activo and ActivoDTO objects.
 * It is annotated with @Component to indicate that it's a Bean and can be autowired where needed.
 */
@Component
public class ActivoConverter {

    /**
     * Method to convert an Activo entity to an ActivoDTO.
     *
     * @param activo the Activo entity to be converted.
     * @return the converted ActivoDTO.
     */
    public ActivoDTO convertToDTO(Activo activo) {
        ActivoDTO activoDTO = new ActivoDTO();
        activoDTO.setId(activo.getId());
        activoDTO.setNumeroInventario(activo.getNumeroInventario());
        activoDTO.setSerial(activo.getSerial());
        activoDTO.setModelo(activo.getModelo());
        activoDTO.setDescripcion(activo.getDescripcion());
        activoDTO.setEstado(activo.getEstado());
        activoDTO.setFechaCreacion(activo.getFechaCreacion());
        activoDTO.setFechaActualizacion(activo.getFechaActualizacion());
        return activoDTO;
    }

    /**
     * Method to convert an ActivoDTO to an Activo entity.
     *
     * @param activoDTO the ActivoDTO to be converted.
     * @return the converted Activo entity.
     */
    public Activo convertToEntity(ActivoDTO activoDTO) {
        Activo activo = new Activo();
        activo.setId(activoDTO.getId());
        activo.setNumeroInventario(activoDTO.getNumeroInventario());
        activo.setSerial(activoDTO.getSerial());
        activo.setModelo(activoDTO.getModelo());
        activo.setDescripcion(activoDTO.getDescripcion());
        activo.setEstado(activoDTO.getEstado());
        activo.setFechaCreacion(activoDTO.getFechaCreacion());
        activo.setFechaActualizacion(activoDTO.getFechaActualizacion());
        return activo;
    }

}
