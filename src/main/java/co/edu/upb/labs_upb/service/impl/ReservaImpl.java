package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.converter.ReservasAulaConverter;
import co.edu.upb.labs_upb.dto.ReservasAulaDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Aula;
import co.edu.upb.labs_upb.model.FechaReserva;
import co.edu.upb.labs_upb.model.ReservaDeAula;
import co.edu.upb.labs_upb.repository.IAulaRepository;
import co.edu.upb.labs_upb.repository.IFechasReservasRepository;
import co.edu.upb.labs_upb.repository.IReservasRepository;
import co.edu.upb.labs_upb.service.iface.IReservaService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservaImpl implements IReservaService {


    private final IReservasRepository reservasRepository;
    private final IFechasReservasRepository fechasReservasRepository;
    private final IAulaRepository aulaRepository;
    private final ReservasAulaConverter reservasConverter;


    public ReservaImpl(IReservasRepository reservasRepository,
                       IFechasReservasRepository fechasReservasRepository,
                       IAulaRepository aulaRepository,
                       ReservasAulaConverter reservasConverter) {

        this.reservasRepository = reservasRepository;
        this.fechasReservasRepository = fechasReservasRepository;
        this.aulaRepository = aulaRepository;
        this.reservasConverter = reservasConverter;

    }


    @Override
    public Map<String, Object> getAllReservas() throws RestException {

        Map<String, Object> response = new HashMap<>();

        List<ReservaDeAula> reservas = reservasRepository.findAll();
        if (reservas.isEmpty()) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontraron reservas",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        Set<ReservasAulaDTO> reservasAulaDTOS = reservas.stream()
                .map(reserva -> reservasConverter.reservaToReservaDTO(reserva))
                .collect(Collectors.toSet());

        reservasAulaDTOS.forEach(reservaDTO -> {
            Set<FechaReserva> fechasByReserva = fechasReservasRepository.findByIdReserva(reservaDTO.getId());
            reservaDTO.setFechasReserva(
                    fechasByReserva.stream()
                            .map(fecha -> {
                                Map<String, Object> fechaMap = new HashMap<>();
                                fechaMap.put("id", fecha.getId());
                                fechaMap.put("inicio", fecha.getHoraInicio());
                                fechaMap.put("fin", fecha.getHoraFin());

                                return fechaMap;

                            }).collect(Collectors.toSet())

            );
        });

        response.put("reservas", reservasAulaDTOS);

        return response;
    }

    @Override
    public Map<String, Object> getReservasByAula(Long idAula) throws RestException {

        Aula aula = aulaRepository.findById(idAula).orElse(null);
        if (aula == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontró el aula",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        Set<ReservaDeAula> reservas = reservasRepository.findByAula(idAula);
        if (reservas.isEmpty()) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontraron reservas para el aula",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        Map<String, Object> response = new HashMap<>();

        Set<ReservasAulaDTO> reservasAulaDTOS = reservas.stream()
                .map(reservasConverter::reservaToReservaDTO)
                .collect(Collectors.toSet());

        reservasAulaDTOS.forEach(reservaDTO -> {
            Set<FechaReserva> fechasByReserva = fechasReservasRepository.findByIdReserva(reservaDTO.getId());
            reservaDTO.setFechasReserva(
                    fechasByReserva.stream()
                            .map(fecha -> {
                                Map<String, Object> fechaMap = new HashMap<>();
                                fechaMap.put("id", fecha.getId());
                                fechaMap.put("inicio", fecha.getHoraInicio());
                                fechaMap.put("fin", fecha.getHoraFin());

                                return fechaMap;

                            }).collect(Collectors.toSet())

            );
        });

        response.put("reservas", reservasAulaDTOS);

        return response;
    }

    @Override
    public Map<String, Object> getReservasById(Long idReserva) throws RestException {

        Optional<ReservaDeAula> reserva = reservasRepository.findById(idReserva);
        if (reserva.isEmpty()) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontró la reserva",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        ReservasAulaDTO reservaDTO = reservasConverter.reservaToReservaDTO(reserva.get());

        Set<FechaReserva> fechasByReserva = fechasReservasRepository.findByIdReserva(reservaDTO.getId());
        reservaDTO.setFechasReserva(
                fechasByReserva.stream()
                        .map(fecha -> {
                            Map<String, Object> fechaMap = new HashMap<>();
                            fechaMap.put("id", fecha.getId());
                            fechaMap.put("inicio", fecha.getHoraInicio());
                            fechaMap.put("fin", fecha.getHoraFin());

                            return fechaMap;

                        }).collect(Collectors.toSet())

        );

        return Map.of("reserva", reservaDTO);
    }

    @Override
    public ReservasAulaDTO createReserva(ReservasAulaDTO reservaDTO) throws RestException {

        if (reservaDTO == null) {
            throw new RestException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "La reserva no puede ser nula",
                            HttpStatus.BAD_REQUEST.value()
                    )
            );
        }

        Aula aula = aulaRepository.findByNumeroInTheSameBloque(reservaDTO.getAula(), reservaDTO.getBloque());
        if (aula == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontró el aula",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        ReservaDeAula reserva = reservasConverter.reservaDTOToReserva(reservaDTO);
        if (reservaDTO.getFechasReserva().isEmpty()) {
            throw new RestException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "La reserva debe tener al menos una fecha",
                            HttpStatus.BAD_REQUEST.value()
                    )
            );
        }

        reserva.setAula(aula);
        reserva.setBloque(aula.getBloque().getNumero());

        LocalDateTime now = LocalDateTime.now();

        reserva.setFechaCreacion(now);
        reserva.setFechaActualizacion(now);


        for (Map<String, Object> fecha : reservaDTO.getFechasReserva()) {

            String inicio = (String) fecha.get("inicio");

            List<ReservaDeAula> datesExist = reservasRepository.getByDateAndAula(inicio, aula.getNumero());

            if (!datesExist.isEmpty()) {
                throw new RestException(
                        ErrorDto.getErrorDto(
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                ConstUtil.MESSAGE_ALREADY,
                                HttpStatus.BAD_REQUEST.value()
                        )
                );
            }

        }

        ReservaDeAula newReserva = reservasRepository.save(reserva);


        Set<FechaReserva> fechasReserva = new HashSet<>();

        for (Map<String, Object> fecha : reservaDTO.getFechasReserva()) {

            FechaReserva fechaReservaTemp = new FechaReserva();

            String inicio = (String) fecha.get("inicio");
            String fin = (String) fecha.get("fin");

            fechaReservaTemp.setHoraInicio(LocalDateTime.parse(inicio));
            fechaReservaTemp.setHoraFin(LocalDateTime.parse(fin));
            fechaReservaTemp.setReservaDeAula(newReserva);

            fechasReserva.add(fechasReservasRepository.save(fechaReservaTemp));

        }


        newReserva.setFechasReserva(fechasReserva);

        reservaDTO.setId(newReserva.getId());

        return reservaDTO;
    }


    @Override
    public ReservasAulaDTO updateDatesReserva(Set<Map<String, Object>> nuevasFechas, Long idReserva) throws RestException {

        if (nuevasFechas.isEmpty()) {
            throw new RestException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "La reserva debe tener al menos una fecha",
                            HttpStatus.BAD_REQUEST.value()
                    )
            );
        }

        ReservaDeAula reserva = reservasRepository.findById(idReserva).orElse(null);

        if (reserva == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontró la reserva",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        Set<FechaReserva> fechasDeLaReserva = fechasReservasRepository.findByIdReserva(reserva.getId());
        if (fechasDeLaReserva.isEmpty()) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontraron fechas para la reserva",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        fechasReservasRepository.deleteAll(fechasDeLaReserva);


        for (Map<String, Object> fecha : nuevasFechas) {

            String inicio = (String) fecha.get("inicio");
            String fin = (String) fecha.get("fin");

            List<ReservaDeAula> datesExist = reservasRepository.getByDateAndAula(inicio, reserva.getAula().getNumero());

            if (!datesExist.isEmpty()) {
                throw new RestException(
                        ErrorDto.getErrorDto(
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                ConstUtil.MESSAGE_ALREADY,
                                HttpStatus.BAD_REQUEST.value()
                        )
                );
            }

            FechaReserva nuevaFecha = new FechaReserva();
            nuevaFecha.setHoraInicio(LocalDateTime.parse(inicio));
            nuevaFecha.setHoraFin(LocalDateTime.parse(fin));

            nuevaFecha.setReservaDeAula(reserva);

            FechaReserva fechaGuardada = fechasReservasRepository.save(nuevaFecha);

            fechasDeLaReserva.add(fechaGuardada);
        }


        reserva.setFechasReserva(fechasDeLaReserva);


        return reservasConverter.reservaToReservaDTO(reserva);
    }

    @Override
    public void deleteReserva(Long idReserva) throws RestException {

        ReservaDeAula reserva = reservasRepository.findById(idReserva).orElse(null);

        if (reserva == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontró la reserva",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        Set<FechaReserva> fechasDeLaReserva = fechasReservasRepository.findByIdReserva(reserva.getId());

        if (fechasDeLaReserva.isEmpty()) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontraron fechas para la reserva",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        HashMap<String, Object> response = new HashMap<>();

        fechasReservasRepository.deleteAll(fechasDeLaReserva);
        reservasRepository.delete(reserva);

    }
}
