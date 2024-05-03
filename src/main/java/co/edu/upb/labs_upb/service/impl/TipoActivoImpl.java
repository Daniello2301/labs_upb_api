package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.converter.TipoActivoConverter;
import co.edu.upb.labs_upb.dto.TipoActivoDTO;
import co.edu.upb.labs_upb.exception.BadRequestException;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.TipoActivo;
import co.edu.upb.labs_upb.repository.ITipoActivoRepository;
import co.edu.upb.labs_upb.service.iface.ITipoActivoService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TipoActivoImpl is a service class that implements the ITipoActivoService interface.
 * It provides methods for managing TipoActivo objects, including CRUD operations and other operations.
 */
@Service
public class TipoActivoImpl implements ITipoActivoService {


    @Autowired
    private ITipoActivoRepository tipoActivoRepository;

    @Autowired
    private TipoActivoConverter tipoActivoConverter;

    /**
     * Retrieves all TipoActivoDTO objects.
     *
     * @return a List of TipoActivoDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public List<TipoActivoDTO> findAllTipoActivo() throws RestException {

        // Find all TipoActivo objects in the database.
        List<TipoActivo> tiposEncontrados = tipoActivoRepository.findAll();
        // If no TipoActivo objects are found, throw a NotFoundException.
        if (tiposEncontrados.isEmpty()) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontraron tipos de activos",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        // Convert the TipoActivo objects to TipoActivoDTO objects and return them.
        return tiposEncontrados.stream()
                .map(tipo -> tipoActivoConverter.tipoActivoToTipoActivoDTO(tipo))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a TipoActivoDTO object by its ID.
     *
     * @param id the ID of the TipoActivoDTO to retrieve.
     * @return a TipoActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public TipoActivoDTO findTipoActivoById(Long id) throws RestException {

        // Find the TipoActivo object by its ID.
        TipoActivo tipoEncontrado = tipoActivoRepository.findById(id).orElse(null);
        if (tipoEncontrado == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontró el tipo de activo",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        // Convert the TipoActivo object to a TipoActivoDTO object and return it.
        return tipoActivoConverter.tipoActivoToTipoActivoDTO(tipoEncontrado);
    }

    /**
     * Retrieves a TipoActivoDTO object by its nomenclatura.
     *
     * @param nomenclatura the nomenclatura of the TipoActivoDTO to retrieve.
     * @return a TipoActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public TipoActivoDTO findByNomenclatura(String nomenclatura) throws RestException {

        TipoActivo tipoEncontrado = tipoActivoRepository.findByNomenclatura(nomenclatura);
        if (tipoEncontrado == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontró el tipo de activo",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        return tipoActivoConverter.tipoActivoToTipoActivoDTO(tipoEncontrado);
    }

    /**
     * Creates a new TipoActivoDTO object.
     *
     * @param tipoActivoDto the TipoActivoDTO object to create.
     * @return the created TipoActivoDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public TipoActivoDTO saveTipoActivo(TipoActivoDTO tipoActivoDto) throws RestException {

        // If the TipoActivoDTO object is null, throw a BadRequestException.
        if (tipoActivoDto == null) {
            throw new RestException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            ConstUtil.MESSAGE_ERROR_DATA,
                            HttpStatus.INTERNAL_SERVER_ERROR.value()
                    )
            );
        }

        // If the TipoActivoDTO object has an ID, check if it exists in the database.
        if (tipoActivoDto.getId() != null) {
            boolean exist = tipoActivoRepository.existsById(tipoActivoDto.getId());
            if (exist) {
                // If the TipoActivoDTO object exists, update it.
                TipoActivo tipoActivoActualizado = tipoActivoConverter.tipoActivoDTOToTipoActivo(tipoActivoDto);
                return tipoActivoConverter.tipoActivoToTipoActivoDTO(tipoActivoRepository.save(tipoActivoActualizado));
            }
        }

        // Check if a TipoActivo object with the same nomenclatura already exists in the database.
        TipoActivo tipoEncontrado = tipoActivoRepository.findByNomenclatura(tipoActivoDto.getNomenclatura());
        if (tipoEncontrado != null) {
            throw new BadRequestException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "Ya existe un tipo de activo con la nomenclatura " + tipoActivoDto.getNomenclatura(),
                            HttpStatus.BAD_REQUEST.value()
                    )
            );
        }

        // Create a new TipoActivo object to save the DTO tipoActivo
        TipoActivo nuevoTipoActivo = tipoActivoConverter.tipoActivoDTOToTipoActivo(tipoActivoDto);

        // Save the new TipoActivo object in the database.
        nuevoTipoActivo = tipoActivoRepository.save(nuevoTipoActivo);

        tipoActivoDto.setId(nuevoTipoActivo.getId());

        return tipoActivoDto;
    }

    /**
     * Deletes a TipoActivoDTO object by its ID.
     *
     * @param id the TipoActivoDTO object to update.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public void deleteTipoActivo(Long id) throws RestException {

            // Find the TipoActivo object by its ID.
            TipoActivo tipoEncontrado = tipoActivoRepository.findById(id).orElse(null);
            if (tipoEncontrado == null) {
                throw new NotFoundException(
                        ErrorDto.getErrorDto(
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                "No se encontró el tipo de activo",
                                HttpStatus.NOT_FOUND.value()
                        )
                );
            }

            tipoActivoRepository.delete(tipoEncontrado);
    }
}
