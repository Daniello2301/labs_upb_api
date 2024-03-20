package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.converter.ActivoConverter;
import co.edu.upb.labs_upb.dto.ActivoDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.*;
import co.edu.upb.labs_upb.repository.*;
import co.edu.upb.labs_upb.service.iface.IActivoService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivoImpl implements IActivoService {

    @Autowired
    private IActivoRepository activoRepository;

    @Autowired
    private ActivoConverter activoConverter;

    @Autowired
    private ITipoActivoRepository tipoActivoRepository;

    @Autowired
    private IAulaRepository aulaRepository;

    @Autowired
    private IBloqueRepository bloqueRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ActivoDTO> activosPagination(int numPage, int sizePage) throws RestException {

        Pageable pageable = PageRequest.of(numPage, sizePage);

        return getActivos(pageable);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> activosEnable(int page, int size, String sortby) throws RestException {

        Pageable paging = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.ASC, sortby));

        Page<Activo> activosEncontrados = activoRepository.finByEstado(true, paging);
        if(activosEncontrados.isEmpty())
        {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        List<Activo> activosResponse = activosEncontrados.getContent();


        List<ActivoDTO> activoDTOS = activosResponse.stream()
                                    .map(activo -> activoConverter.convertToDTO(activo))
                                    .toList();

        for(Activo activo: activosResponse){
            TipoActivo tipoActivo = tipoActivoRepository.findByNomenclatura(activo.getTipoActivo().getNomenclatura());
            Aula aula = aulaRepository.findByNumeroInTheSameBloque(activo.getAula().getNumero(), activo.getBloque().getNumero());
            Bloque bloque = bloqueRepository.findByNumero(activo.getBloque().getNumero());
            Usuario usuario = usuarioRepository.findByIdUpb(activo.getUsuario().getIdUpb());

            activoDTOS.forEach(activoDTO -> {
                if(Objects.equals(activoDTO.getId(), activo.getId())){
                    activoDTO.setTipoActivo(tipoActivo.getNomenclatura());
                    activoDTO.setAula(aula.getNumero());
                    activoDTO.setBloque(bloque.getNumero());
                    activoDTO.setUsuario(usuario.getIdUpb());
                }

            });
        }

        Map<String, Object> response = new HashMap<>();
        response.put("Items", activoDTOS);
        response.put("currentPage", activosEncontrados.getNumber());
        response.put("totalItems", activosEncontrados.getTotalElements());
        response.put("totalPages", activosEncontrados.getTotalPages());
        response.put("pageable", activosEncontrados.getPageable());
        response.put("sort", activosEncontrados.getPageable().getSort());


        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivoDTO> activosPaginationSortBy(int numPage, int sizePage, String sortBy) throws RestException {
        Pageable pageable = PageRequest.of(numPage, sizePage).withSort(Sort.by(Sort.Direction.ASC, sortBy));

        return getActivos(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivoDTO> getAll() throws RestException {

        List<Activo> activos = activoRepository.findAll();
        if(activos.isEmpty())
        {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        List<ActivoDTO> activoDTOS = activos.stream()
                                    .map(activo -> activoConverter.convertToDTO(activo))
                                    .toList();

        for(Activo activo: activos)
        {
            TipoActivo tipoActivo = tipoActivoRepository.findByNomenclatura(activo.getTipoActivo().getNomenclatura());
            Aula aula = aulaRepository.findByNumeroInTheSameBloque(activo.getAula().getNumero(), activo.getBloque().getNumero());
            Bloque bloque = bloqueRepository.findByNumero(activo.getBloque().getNumero());
            Usuario usuario = usuarioRepository.findByIdUpb(activo.getUsuario().getIdUpb());

            activoDTOS.forEach(activoDTO -> {
                if(Objects.equals(activoDTO.getId(), activo.getId())){
                    activoDTO.setTipoActivo(tipoActivo.getNomenclatura());
                    activoDTO.setAula(aula.getNumero());
                    activoDTO.setBloque(bloque.getNumero());
                    activoDTO.setUsuario(usuario.getIdUpb());
                }

            });

        }

        return activoDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ActivoDTO> getByUsuarioId(String numeroInventario) throws RestException {

        Set<Activo> activos  = activoRepository.findByUsuarioIdUpb(numeroInventario);

        if(activos.isEmpty())
        {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        Set<ActivoDTO> activoDTOS = activos.stream()
                                    .map(activo -> activoConverter.convertToDTO(activo))
                                    .collect(Collectors.toSet());

        activos.forEach(activo -> {
            TipoActivo tipoActivo = tipoActivoRepository.findByNomenclatura(activo.getTipoActivo().getNomenclatura());
            Aula aula = aulaRepository.findByNumeroInTheSameBloque(activo.getAula().getNumero(), activo.getBloque().getNumero());
            Bloque bloque = bloqueRepository.findByNumero(activo.getBloque().getNumero());
            Usuario usuario = usuarioRepository.findByIdUpb(activo.getUsuario().getIdUpb());
            activoDTOS.forEach(activoDTO -> {
                if (Objects.equals(activoDTO.getId(), activo.getId())) {
                    activoDTO.setTipoActivo(tipoActivo.getNomenclatura());
                    activoDTO.setAula(aula.getNumero());
                    activoDTO.setBloque(bloque.getNumero());
                    activoDTO.setUsuario(usuario.getIdUpb());
                }

            });
        });

        return activoDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public ActivoDTO getById(Long id) throws RestException {

        Activo activo = activoRepository.findById(id).orElse(null);
        if(activo == null)
        {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        ActivoDTO activoDTO = activoConverter.convertToDTO(activo);

        TipoActivo tipoActivo = tipoActivoRepository.findByNomenclatura(activo.getTipoActivo().getNomenclatura());
        validarBusqueda(tipoActivo);
        Aula aula = aulaRepository.findByNumeroInTheSameBloque(activo.getAula().getNumero(), activo.getBloque().getNumero());
        validarBusqueda(aula);
        Bloque bloque = bloqueRepository.findByNumero(activo.getBloque().getNumero());
        validarBusqueda(bloque);
        Usuario usuario = usuarioRepository.findByIdUpb(activo.getUsuario().getIdUpb());
        validarBusqueda(usuario);

        activoDTO.setTipoActivo(tipoActivo.getNomenclatura());
        activoDTO.setAula(aula.getNumero());
        activoDTO.setBloque(bloque.getNumero());
        activoDTO.setUsuario(usuario.getIdUpb());

        return activoDTO;
    }

    @Override
    @Transactional
    public ActivoDTO create(ActivoDTO activoDto) throws RestException {

        // If the activoDto is null
        if(activoDto == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            ConstUtil.MESSAGE_NOT_FOUND,
                            HttpStatus.BAD_REQUEST.value()));
        }

        // Convert to DTO to Entity
        Activo activoNuevo = activoConverter.convertToEntity(activoDto);

        // If the aula is not in the same bloque
        Aula aulaEncontrada = aulaRepository.findByNumeroInTheSameBloque(activoDto.getAula(), activoDto.getBloque());
        if(aulaEncontrada == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "El aula no se encuentra en el bloque",
                            HttpStatus.BAD_REQUEST.value()));
        }

        // If the tipoActivo, aula, bloque or usuario not found
        TipoActivo tipoActivoEncontrado = tipoActivoRepository.findByNomenclatura(activoDto.getTipoActivo());
        if(tipoActivoEncontrado == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "El tipo de activo no existe",
                            HttpStatus.NOT_FOUND.value()));
        }
        Bloque bloqueEncontrado = bloqueRepository.findByNumero(activoDto.getBloque());
        if(bloqueEncontrado == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "El bloque no existe",
                            HttpStatus.NOT_FOUND.value()));
        };
        Usuario usuarioEncontrado = usuarioRepository.findByIdUpb(activoDto.getUsuario());
        if(usuarioEncontrado == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "El usuario no existe",
                            HttpStatus.NOT_FOUND.value()));
        }


        // Set the tipoActivo, aula, bloque and usuario to the new activo
        activoNuevo.setTipoActivo(tipoActivoEncontrado);
        activoNuevo.setAula(aulaEncontrada);
        activoNuevo.setBloque(bloqueEncontrado);
        activoNuevo.setUsuario(usuarioEncontrado);

        // If we get the activo with the id, we update the activo
        if(activoDto.getId() != null){
            boolean activoExiste = activoRepository.existsById(activoDto.getId());
            if(activoExiste){
                return activoConverter.convertToDTO(activoRepository.save(activoNuevo));
            }
        }

        // if the number of inventory already exists
        Activo existeByNumInventario = activoRepository.findByNumeroInventario(activoDto.getNumeroInventario());
        if(existeByNumInventario != null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "El número de inventario ya existe",
                            HttpStatus.BAD_REQUEST.value()));
        }


        activoRepository.save(activoNuevo);

        activoDto.setId(activoNuevo.getId());

        return activoDto;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        activoRepository.deleteById(id);
    }

    private void validarBusqueda(Object object) throws RestException {
        if(object == null)
        {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            ConstUtil.MESSAGE_NOT_FOUND,
                            HttpStatus.NOT_FOUND.value()));
        }
    }

    private Page<ActivoDTO> getActivos(Pageable pageable) throws NotFoundException {
        Page<Activo> activos = activoRepository.findAll(pageable);

        if(activos.isEmpty())
        {
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }

        Page<ActivoDTO> activoDTOS = activos.map(activo -> activoConverter.convertToDTO(activo));

        for(Activo activo: activos)
        {
            TipoActivo tipoActivo = tipoActivoRepository.findByNomenclatura(activo.getTipoActivo().getNomenclatura());
            Aula aula = aulaRepository.findByNumeroInTheSameBloque(activo.getAula().getNumero(), activo.getBloque().getNumero());
            Bloque bloque = bloqueRepository.findByNumero(activo.getBloque().getNumero());
            Usuario usuario = usuarioRepository.findByIdUpb(activo.getUsuario().getIdUpb());

            activoDTOS.forEach(activoDTO -> {
                if(Objects.equals(activoDTO.getId(), activo.getId())){
                    activoDTO.setTipoActivo(tipoActivo.getNomenclatura());
                    activoDTO.setAula(aula.getNumero());
                    activoDTO.setBloque(bloque.getNumero());
                    activoDTO.setUsuario(usuario.getIdUpb());
                }

            });

        }

        return activoDTOS;
    }
}
