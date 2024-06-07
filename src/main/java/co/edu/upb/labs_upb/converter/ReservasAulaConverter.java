package co.edu.upb.labs_upb.converter;

import co.edu.upb.labs_upb.dto.ReservasAulaDTO;
import co.edu.upb.labs_upb.model.ReservaDeAula;
import org.springframework.stereotype.Component;

@Component
public class ReservasAulaConverter {

    public ReservasAulaDTO reservaToReservaDTO(ReservaDeAula reserva) {
        ReservasAulaDTO reservaDTO = new ReservasAulaDTO();
        reservaDTO.setId(reserva.getId());
        reservaDTO.setAula(reserva.getAula().getNumero() == null ? 0 : reserva.getAula().getNumero());
        reservaDTO.setBloque(reserva.getBloque() == null ? 0 : reserva.getBloque());
        reservaDTO.setPersona(reserva.getPersona());
        reservaDTO.setDescripcion(reserva.getDescripcion());
        reservaDTO.setEstado(reserva.getEstado());

        return reservaDTO;

    }

    public ReservaDeAula reservaDTOToReserva(ReservasAulaDTO reservaDTO) {
        ReservaDeAula reserva = new ReservaDeAula();
        reserva.setId(reservaDTO.getId());
        reserva.setPersona(reservaDTO.getPersona());
        reserva.setDescripcion(reservaDTO.getDescripcion());
        reserva.setEstado(reservaDTO.getEstado());

        return reserva;

    }

}
