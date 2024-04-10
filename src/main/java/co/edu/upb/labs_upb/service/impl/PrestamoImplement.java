package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.converter.ActivoConverter;
import co.edu.upb.labs_upb.converter.PrestamoConverter;
import co.edu.upb.labs_upb.dto.ActivoDTO;
import co.edu.upb.labs_upb.dto.PrestamoDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.InternalServerErrorException;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.*;
import co.edu.upb.labs_upb.repository.IActivoRepository;
import co.edu.upb.labs_upb.repository.IPrestamoRepository;
import co.edu.upb.labs_upb.service.iface.IActivoService;
import co.edu.upb.labs_upb.service.iface.IPrestamoService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PrestamoImplement implements IPrestamoService {


    @Autowired
    private IPrestamoRepository prestamoRepository;

    @Autowired
    private IActivoRepository activoRepository;

    @Autowired
    private PrestamoConverter prestamoConverter;

    @Autowired
    private ActivoConverter activoConverter;

    @Autowired
    private IActivoService activoService;

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoDTO> getAll() throws RestException {

        List<Prestamo> prestamos = prestamoRepository.findAll();

        if(prestamos.isEmpty()){
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()));
        }

        List<PrestamoDTO> prestamosDTO = new ArrayList<>();

        System.out.println(prestamos);
        for(Prestamo prestamo : prestamos){
            PrestamoDTO prestamoDTO = prestamoConverter.entityToDto(prestamo);
            Set<String> activos = new HashSet<>();
            for(Activo activo : prestamo.getActivos()){
                activos.add(activo.getNumeroInventario());
                System.out.println(activo.getNumeroInventario());
            }
            prestamoDTO.setActivos(activos);
            prestamosDTO.add(prestamoDTO);
        }


        return prestamosDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoDTO getById(Long id) throws RestException {
        if(id  == null){
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ConstUtil.MESSAGE_ERROR_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
        if(prestamo == null){
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El prestamo con el id " + id + " no existe",
                    HttpStatus.NOT_FOUND.value()));
        }

        PrestamoDTO prestamoDTO = prestamoConverter.entityToDto(prestamo);

        Set<Activo> activosByPrestamo = activoRepository.findByIdPrestamo(prestamo.getId());

        Set<String> activos = new HashSet<>();

        for(Activo activo : activosByPrestamo){
            activos.add(activo.getNumeroInventario());
        }

        prestamoDTO.setActivos(activos);

        return prestamoDTO;
    }

    @Override
    public Map<String, Object> prestamosEnable(int page, int size,String sortby) throws RestException {

        Pageable paging = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.ASC, sortby));

        Page<Prestamo> prestamoEncontrados = prestamoRepository.finByEstado(true, paging);
        if(prestamoEncontrados.isEmpty())
        {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        List<Prestamo> prestamosResponse = prestamoEncontrados.getContent();


        List<PrestamoDTO> prestamosDTO = prestamosResponse.stream()
                .map(prestamo -> prestamoConverter.entityToDto(prestamo))
                .toList();


        Map<String, Object> response = new HashMap<>();
        response.put("Items", prestamosDTO);
        response.put("currentPage", prestamoEncontrados.getNumber());
        response.put("totalItems", prestamoEncontrados.getTotalElements());
        response.put("totalPages", prestamoEncontrados.getTotalPages());
        response.put("pageable", prestamoEncontrados.getPageable());
        response.put("sort", prestamoEncontrados.getPageable().getSort());


        return response;
    }

    @Override
    public PrestamoDTO create(PrestamoDTO prestamoDTO) throws RestException {

        if(prestamoDTO == null){
             throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                     ConstUtil.MESSAGE_ERROR_DATA,
                     HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        Prestamo prestamoConvertido = prestamoConverter.dtoToEntity(prestamoDTO);

        if(prestamoDTO.getId() != null){
            boolean exist = prestamoRepository.existsById(prestamoDTO.getId());
            if(exist){
                return prestamoConverter.entityToDto(prestamoRepository.save(prestamoConvertido));
            }
        }

        Prestamo prestamoNuevo = prestamoRepository.save(prestamoConvertido);
        for(String activo : prestamoDTO.getActivos()){
            if(activo != null){
                Activo activoEncontrado = activoRepository.findByNumeroInventario(activo);
                if(activoEncontrado == null){
                    throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "El activo con el numero de inventario " + activo + " no existe",
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
                }
                activoEncontrado.setPrestamo(prestamoNuevo);

                activoRepository.updatePrestamo(prestamoNuevo.getId(), activoEncontrado.getNumeroInventario());
            }
        }


        prestamoDTO.setId(prestamoNuevo.getId());

        return prestamoDTO;
    }

    @Override
    public void addActivo(Long id, String numeroInventario) throws RestException {

        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
        if(prestamo == null ){
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El prestamo con el id " + id + " no existe",
                    HttpStatus.NOT_FOUND.value()));
        }

        if(!prestamo.getEstado()){
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El prestamo con el id " + id + " ya fue cerrado",
                    HttpStatus.NOT_FOUND.value()));
        }

        Activo activo = activoRepository.findByNumeroInventario(numeroInventario);
        if(activo == null){
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El activo con el numero de inventario " + numeroInventario + " no existe",
                    HttpStatus.NOT_FOUND.value()));
        }

        activoRepository.updatePrestamo(prestamo.getId(), activo.getNumeroInventario());
    }

    @Override
    public void closePrestamo(Long id) throws RestException {

        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);
        if(prestamo == null){
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "El prestamo con el id " + id + " no existe",
                    HttpStatus.NOT_FOUND.value()));
        }

        prestamo.setFechaEntrega(LocalDateTime.now());

        prestamoRepository.closePrestamo(prestamo.getId());

        Set<Activo> activosFromPrestamo = activoRepository.findByIdPrestamo(prestamo.getId());

        for (Activo activo : activosFromPrestamo) {
            Activo activoEncontrado = activoRepository.findByNumeroInventario(activo.getNumeroInventario());
            if (activoEncontrado == null) {
                throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "El activo con el numero de inventario " + activo.getNumeroInventario() + " no existe",
                        HttpStatus.NOT_FOUND.value()));
            }
            activoRepository.updatePrestamo(null, activo.getNumeroInventario());
        }
    }
}
