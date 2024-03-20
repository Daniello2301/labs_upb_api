package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.PrestamoDTO;
import co.edu.upb.labs_upb.model.Prestamo;
import org.springframework.stereotype.Component;

@Component
public class PrestamoConverter {

    public PrestamoDTO entityToDto(Prestamo prestamo) {
        PrestamoDTO prestamoDTO = new PrestamoDTO();
        prestamoDTO.setId(prestamo.getId());
        prestamoDTO.setFechaSalida(prestamo.getFechaSalida());
        prestamoDTO.setFechaEntrega(prestamo.getFechaEntrega());
        prestamoDTO.setLaboratorio(prestamo.getLaboratorio());
        prestamoDTO.setCentroCostos(prestamo.getCentroCostos());
        prestamoDTO.setFacultad(prestamo.getFacultad());
        prestamoDTO.setEstado(prestamo.getEstado());
        prestamoDTO.setIdPersona(prestamo.getIdPersona());
        prestamoDTO.setNombrePersona(prestamo.getNombrePersona());
        prestamoDTO.setFechaCreacion(prestamo.getFechaCreacion());
        prestamoDTO.setFechaActualizacion(prestamo.getFechaActualizacion());
        return prestamoDTO;
    }


    public Prestamo dtoToEntity(PrestamoDTO prestamoDTO) {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(prestamoDTO.getId());
        prestamo.setFechaSalida(prestamoDTO.getFechaSalida());
        prestamo.setFechaEntrega(prestamoDTO.getFechaEntrega());
        prestamo.setLaboratorio(prestamoDTO.getLaboratorio());
        prestamo.setCentroCostos(prestamoDTO.getCentroCostos());
        prestamo.setFacultad(prestamoDTO.getFacultad());
        prestamo.setEstado(prestamoDTO.getEstado());
        prestamo.setIdPersona(prestamoDTO.getIdPersona());
        prestamo.setNombrePersona(prestamoDTO.getNombrePersona());
        prestamo.setFechaCreacion(prestamoDTO.getFechaCreacion());
        prestamo.setFechaActualizacion(prestamoDTO.getFechaActualizacion());
        return prestamo;
    }

}
