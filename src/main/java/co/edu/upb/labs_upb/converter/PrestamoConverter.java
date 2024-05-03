package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.PrestamoDTO;
import co.edu.upb.labs_upb.model.Prestamo;
import org.springframework.stereotype.Component;

/**
 * PrestamoConverter is a component that provides methods to convert between Prestamo and PrestamoDTO objects.
 * It is annotated with @Component to indicate that it's a Bean and can be autowired where needed.
 */
@Component
public class PrestamoConverter {

    /**
     * Method to convert a Prestamo entity to a PrestamoDTO.
     *
     * @param prestamo the Prestamo entity to be converted.
     * @return the converted PrestamoDTO.
     */
    public PrestamoDTO entityToDto(Prestamo prestamo) {
        PrestamoDTO prestamoDTO = new PrestamoDTO();
        prestamoDTO.setId(prestamo.getId());
        prestamoDTO.setNumeroPrestamo(prestamo.getNumeroPrestamo());
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


    /**
     * Method to convert a PrestamoDTO to a Prestamo entity.
     *
     * @param prestamoDTO the PrestamoDTO to be converted.
     * @return the converted Prestamo entity.
     */
    public Prestamo dtoToEntity(PrestamoDTO prestamoDTO) {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(prestamoDTO.getId());
        prestamo.setNumeroPrestamo(prestamoDTO.getNumeroPrestamo());
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
