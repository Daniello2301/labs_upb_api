package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.TipoActivoDTO;
import co.edu.upb.labs_upb.model.TipoActivo;
import org.springframework.stereotype.Component;

/**
 * TipoActivoConverter is a component that provides methods to convert between TipoActivo and TipoActivoDTO objects.
 * It is annotated with @Component to indicate that it's a Bean and can be autowired where needed.
 */
@Component
public class TipoActivoConverter {

    /**
     * Method to convert a TipoActivo entity to a TipoActivoDTO.
     *
     * @param tipoActivo the TipoActivo entity to be converted.
     * @return the converted TipoActivoDTO.
     */
    public TipoActivoDTO tipoActivoToTipoActivoDTO(co.edu.upb.labs_upb.model.TipoActivo tipoActivo) {

        TipoActivoDTO tipoActivoDTO = new TipoActivoDTO();

        tipoActivoDTO.setId(tipoActivo.getId());
        tipoActivoDTO.setNomenclatura(tipoActivo.getNomenclatura());
        tipoActivoDTO.setDescripcion(tipoActivo.getDescripcion());
        tipoActivoDTO.setFechaCreacion(tipoActivo.getFechaCreacion());
        tipoActivoDTO.setFechaActualizacion(tipoActivo.getFechaActualizacion());

        return tipoActivoDTO;

    }


    /**
     * Method to convert a TipoActivoDTO to a TipoActivo entity.
     *
     * @param tipoActivoDTO the TipoActivoDTO to be converted.
     * @return the converted TipoActivo entity.
     */
    public TipoActivo tipoActivoDTOToTipoActivo(TipoActivoDTO tipoActivoDTO) {

        TipoActivo tipoActivo = new TipoActivo();

        tipoActivo.setId(tipoActivoDTO.getId());
        tipoActivo.setNomenclatura(tipoActivoDTO.getNomenclatura());
        tipoActivo.setDescripcion(tipoActivoDTO.getDescripcion());
        tipoActivo.setFechaCreacion(tipoActivoDTO.getFechaCreacion());
        tipoActivo.setFechaActualizacion(tipoActivoDTO.getFechaActualizacion());

        return tipoActivo;

    }



}
