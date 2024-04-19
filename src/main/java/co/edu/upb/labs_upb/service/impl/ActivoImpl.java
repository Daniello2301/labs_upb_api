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

/**
 * ActivoImpl is a service class that implements the IActivoService interface.
 * It provides methods for managing Activo objects, including CRUD operations and pagination.
 */
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

    /**
     * Retrieves a paginated list of ActivoDTO objects.
     *
     * @param numPage the number of the page to retrieve.
     * @param sizePage the size of the page to retrieve.
     * @return a Page of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActivoDTO> activosPagination(int numPage, int sizePage) throws RestException {

        Pageable pageable = PageRequest.of(numPage, sizePage);

        return getActivos(pageable);
    }

    /**
     * Retrieves a map of Activo objects that are enabled, sorted by a specified attribute.
     *
     * @param page the number of the page to retrieve.
     * @param size the size of the page to retrieve.
     * @param sortby the attribute to sort the Activo objects by.
     * @return a Map of Activo objects.
     * @throws RestException if an error occurs during the operation.
     */
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
                                    .map(activo -> {
                                        try {
                                            return toEntity(activo);
                                        } catch (RestException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .toList();

        Map<String, Object> ActivosResponse = new HashMap<>();
        ActivosResponse.put("Items", activoDTOS);
        ActivosResponse.put("currentPage", activosEncontrados.getNumber());
        ActivosResponse.put("totalItems", activosEncontrados.getTotalElements());
        ActivosResponse.put("totalPages", activosEncontrados.getTotalPages());
        ActivosResponse.put("pageable", activosEncontrados.getPageable());
        ActivosResponse.put("sort", activosEncontrados.getPageable().getSort());


        return ActivosResponse;
    }

    /**
     * Retrieves a paginated and sorted list of ActivoDTO objects.
     *
     * @param numPage the number of the page to retrieve.
     * @param sizePage the size of the page to retrieve.
     * @param sortBy the attribute to sort the ActivoDTO objects by.
     * @return a Page of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActivoDTO> activosPaginationSortBy(int numPage, int sizePage, String sortBy) throws RestException {
        Pageable pageable = PageRequest.of(numPage, sizePage).withSort(Sort.by(Sort.Direction.ASC, sortBy));

        return getActivos(pageable);
    }


    /**
     * Retrieves all ActivoDTO objects.
     *
     * @return a List of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ActivoDTO> getAll() throws RestException {

        // Retrieve all 'Activo' entities from the database using 'activoRepository'
        List<Activo> activos = activoRepository.findAll();

        // Check if the retrieved list is empty
        if(activos.isEmpty())
        {
            // Throw a 'NotFoundException' if the list is empty
            throw new NotFoundException(ErrorDto.getErrorDto(HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ConstUtil.MESSAGE_NOT_FOUND, HttpStatus.NOT_FOUND.value()));
        }


        // Convert the list of 'Activo' entities to a list of 'ActivoDTO' objects using 'activoConverter'
        List<ActivoDTO> activoDTOS = activos.stream()
                                    .map(activo -> {
                                        try {
                                            return toEntity(activo);
                                        } catch (RestException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .toList();
        // Return the list of 'ActivoDTO' objects
        return activoDTOS;
    }

    /**
     * Retrieves all ActivoDTO objects associated with a specific user.
     *
     * @param numeroInventario the ID of the user.
     * @return a Set of ActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
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

        ActivoDTO activoDTO = toEntity(activo);


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
                            "Para el activo: "+ activoDto.getNumeroInventario() +", el aula no se encuentra en el bloque",
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

    private ActivoDTO toEntity(Activo activo) throws RestException {

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

        Page<ActivoDTO> activoDTOS = activos.map(activo -> {
            try {
                return toEntity(activo);
            } catch (RestException e) {
                throw new RuntimeException(e);
            }
        });

        return activoDTOS;
    }



}
