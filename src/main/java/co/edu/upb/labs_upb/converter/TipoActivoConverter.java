package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.TipoActivoDTO;
import co.edu.upb.labs_upb.model.TipoActivo;
import org.springframework.stereotype.Component;

@Component
public class TipoActivoConverter {

    public TipoActivoDTO tipoActivoToTipoActivoDTO(co.edu.upb.labs_upb.model.TipoActivo tipoActivo){
        TipoActivoDTO tipoActivoDTO = new TipoActivoDTO();
        tipoActivoDTO.setId(tipoActivo.getId());
        tipoActivoDTO.setNomenclatura(tipoActivo.getNomenclatura());
        tipoActivoDTO.setDescripcion(tipoActivo.getDescripcion());
        tipoActivoDTO.setFechaCreacion(tipoActivo.getFechaCreacion());
        tipoActivoDTO.setFechaActualizacion(tipoActivo.getFechaActualizacion());
        return tipoActivoDTO;
    }


    public TipoActivo tipoActivoDTOToTipoActivo(TipoActivoDTO tipoActivoDTO){
        TipoActivo tipoActivo = new TipoActivo();
        tipoActivo.setId(tipoActivoDTO.getId());
        tipoActivo.setNomenclatura(tipoActivoDTO.getNomenclatura());
        tipoActivo.setDescripcion(tipoActivoDTO.getDescripcion());
        tipoActivo.setFechaCreacion(tipoActivoDTO.getFechaCreacion());
        tipoActivo.setFechaActualizacion(tipoActivoDTO.getFechaActualizacion());
        return tipoActivo;
    }



}
