package co.edu.upb.labs_upb.service.iface;

import co.edu.upb.labs_upb.dto.ReservasAulaDTO;
import co.edu.upb.labs_upb.exception.RestException;

import java.util.Map;
import java.util.Set;

public interface IReservaService {

    Map<String, Object> getAllReservas() throws RestException;

    Map<String, Object> getReservasByAula(Long idAula) throws RestException;

    Map<String, Object> getReservasById(Long idReserva) throws RestException;

    ReservasAulaDTO createReserva(ReservasAulaDTO reservaDTO) throws RestException;

    ReservasAulaDTO updateDatesReserva(Set<String> fechas, Long idReserva) throws RestException;

    void deleteReserva(Long idReserva) throws RestException;

}
