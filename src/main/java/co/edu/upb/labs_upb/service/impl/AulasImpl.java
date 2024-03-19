package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.converter.AulasConverter;
import co.edu.upb.labs_upb.dto.AulaDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.InternalServerErrorException;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Aula;
import co.edu.upb.labs_upb.model.Bloque;
import co.edu.upb.labs_upb.repository.IAulaRepository;
import co.edu.upb.labs_upb.repository.IBloqueRepository;
import co.edu.upb.labs_upb.service.iface.IAulaService;
import co.edu.upb.labs_upb.utilities.ConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AulasImpl implements IAulaService {

    @Autowired
    private IAulaRepository aulaRepository;

    @Autowired
    private IBloqueRepository bloqueRepository;

    @Autowired
    private AulasConverter aulasConverter;

    @Override
    @Transactional(readOnly = true)
    public List<AulaDTO> findAllAulas() throws RestException {

        try{

            List<Aula> aulas = aulaRepository.findAll();
            if(aulas.isEmpty()){
                throw new NotFoundException(
                        ErrorDto.getErrorDto(
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                "No Se encontraron aulas",
                                HttpStatus.NOT_FOUND.value()
                        )
                );
            }

            List<AulaDTO> aulasDto = aulas.stream()
                    .map(aula -> aulasConverter.aulaToAulaDTO(aula))
                    .collect(Collectors.toList());

            for(Aula aula : aulas){
                Bloque bloque = bloqueRepository.findByNumero(aula.getBloque().getNumero());
                aulasDto.stream()
                        .filter(aulaDto -> aulaDto.getId().equals(aula.getId()))
                        .forEach(aulaDto -> aulaDto.setBloque(bloque.getNumero()));
            }

            return aulasDto;

        }catch (Exception e){
            throw new InternalServerErrorException(
                    ErrorDto.getErrorDto(
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            e.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value()
                    )
            );
        }

    }

    @Override
    public AulaDTO findAulaById(Long id) throws RestException {

        Aula aula = aulaRepository.findById(id).orElse(null);

        if(aula == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontro el aula",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        Bloque bloque = bloqueRepository.findByNumero(aula.getBloque().getNumero());

        AulaDTO aulaDTO = aulasConverter.aulaToAulaDTO(aula);

        aulaDTO.setBloque(bloque.getNumero());

        return aulaDTO;
    }

    @Override
    public List<AulaDTO> findAulaByNumero(Long numero) throws RestException {

        List<Aula> aulas = aulaRepository.findByNumero(numero);

        if(aulas.isEmpty()){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontraron aulas con ese numero",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        List<AulaDTO> aulasDto = aulas.stream()
                .map(aula -> aulasConverter.aulaToAulaDTO(aula))
                .toList();

        for(Aula aula : aulas) {
            Bloque bloque = bloqueRepository.findByNumero(aula.getBloque().getNumero());
            if (bloque == null) {
                throw new NotFoundException(
                        ErrorDto.getErrorDto(
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                "No se encontro el bloque",
                                HttpStatus.NOT_FOUND.value()
                        )
                );
            }
            aulasDto.stream()
                    .filter(aulaDto -> aulaDto.getId().equals(aula.getId()))
                    .forEach(aulaDto -> aulaDto.setBloque(bloque.getNumero()));
        }

        return aulasDto;
    }

    @Override
    public AulaDTO saveAula(AulaDTO aulaDto) throws RestException {

        if(aulaDto == null){
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ConstUtil.MESSAGE_ERROR_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        Bloque bloqueTemp = bloqueRepository.findByNumero(aulaDto.getBloque());
        if(bloqueTemp == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontro el bloque",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        if(aulaDto.getId() != null){
            boolean exist = aulaRepository.existsById(aulaDto.getId());
            if(exist){
                Aula aulaActualizada = aulasConverter.aulaDTOToAula(aulaDto);
                aulaActualizada.setBloque(bloqueTemp);
                return aulasConverter.aulaToAulaDTO(aulaRepository.save(aulaActualizada));
            }
        }

        Aula aulaExistsInBloque = aulaRepository.findByNumeroInTheSameBloque(aulaDto.getNumero(), aulaDto.getBloque());
        if(aulaExistsInBloque != null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "Ya existe un aula con el mismo numero en el bloque",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        Aula newAula = aulasConverter.aulaDTOToAula(aulaDto);
        newAula.setBloque(bloqueTemp);

        newAula = aulaRepository.save(newAula);

        aulaDto.setId(newAula.getId());

        return aulaDto;
    }

    @Override
    public void deleteAula(Long id) throws RestException {

        Aula aulaEncontrado = aulaRepository.findById(id).orElse(null);

        if(aulaEncontrado == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontro el aula",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        aulaRepository.deleteById(id);

    }
}
