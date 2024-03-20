package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.ActivoDTO;
import co.edu.upb.labs_upb.model.Activo;
import org.springframework.stereotype.Component;

@Component
public class ActivoConverter {

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
