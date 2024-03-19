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

@Service
public class TipoActivoImpl implements ITipoActivoService {


    @Autowired
    private ITipoActivoRepository tipoActivoRepository;

    @Autowired
    private TipoActivoConverter tipoActivoConverter;

    @Override
    public List<TipoActivoDTO> findAllTipoActivo() throws RestException {

        List<TipoActivo> tiposEncontrados = tipoActivoRepository.findAll();
        if(tiposEncontrados.isEmpty()){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontraron tipos de activos",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        return tiposEncontrados.stream()
                .map(tipo -> tipoActivoConverter.tipoActivoToTipoActivoDTO(tipo))
                .toList();
    }

    @Override
    public TipoActivoDTO findTipoActivoById(Long id) throws RestException {

        TipoActivo tipoEncontrado = tipoActivoRepository.findById(id).orElse(null);
        if(tipoEncontrado == null){
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

    @Override
    public TipoActivoDTO findByNomenclatura(String nomenclatura) throws RestException {

        TipoActivo tipoEncontrado = tipoActivoRepository.findByNomenclatura(nomenclatura);
        if(tipoEncontrado == null){
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

    @Override
    public TipoActivoDTO saveTipoActivo(TipoActivoDTO tipoActivoDto) throws RestException {

        if(tipoActivoDto == null){
            throw new RestException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            ConstUtil.MESSAGE_ERROR_DATA,
                            HttpStatus.INTERNAL_SERVER_ERROR.value()
                    )
            );
        }

        if(tipoActivoDto.getId() != null){
            boolean exist = tipoActivoRepository.existsById(tipoActivoDto.getId());
            if(exist){
                TipoActivo tipoActivoActualizado = tipoActivoConverter.tipoActivoDTOToTipoActivo(tipoActivoDto);
                return tipoActivoConverter.tipoActivoToTipoActivoDTO(tipoActivoRepository.save(tipoActivoActualizado));
            }
        }

        TipoActivo tipoEncontrado = tipoActivoRepository.findByNomenclatura(tipoActivoDto.getNomenclatura());
        if(tipoEncontrado != null){
            throw new BadRequestException(
                    ErrorDto.getErrorDto(
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "Ya existe un tipo de activo con la nomenclatura " + tipoActivoDto.getNomenclatura(),
                            HttpStatus.BAD_REQUEST.value()
                    )
            );
        }

        TipoActivo nuevoTipoActivo = tipoActivoConverter.tipoActivoDTOToTipoActivo(tipoActivoDto);

        nuevoTipoActivo = tipoActivoRepository.save(nuevoTipoActivo);

        tipoActivoDto.setId(nuevoTipoActivo.getId());

        return tipoActivoDto;
    }

    @Override
    public void deleteTipoActivo(Long id) throws RestException {

            TipoActivo tipoEncontrado = tipoActivoRepository.findById(id).orElse(null);
            if(tipoEncontrado == null){
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
