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

/**
 * AulasImpl is a service class that implements the IAulaService interface.
 * It provides methods for managing Aula objects, including CRUD operations.
 */
@Service
public class AulasImpl implements IAulaService {

    @Autowired
    private IAulaRepository aulaRepository;

    @Autowired
    private IBloqueRepository bloqueRepository;

    @Autowired
    private AulasConverter aulasConverter;

    /**
     * Retrieves all AulaDTO objects.
     *
     * @return a List of AulaDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AulaDTO> findAllAulas() throws RestException {

        try{

            // Retrieve all Aula objects
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

            // Convert Aula objects to AulaDTO objects
            List<AulaDTO> aulasDto = aulas.stream()
                    .map(aula -> aulasConverter.aulaToAulaDTO(aula))
                    .collect(Collectors.toList());

            // Retrieve all Bloque objects
            for(Aula aula : aulas){
                /*
                * Retrieve the Bloque object associated with the Aula object
                * and set the Bloque number to the AulaDTO object.
                * */
                Bloque bloque = bloqueRepository.findByNumero(aula.getBloque().getNumero());
                aulasDto.stream()
                        .filter(aulaDto -> aulaDto.getId().equals(aula.getId()))
                        .forEach(aulaDto -> aulaDto.setBloque(bloque.getNumero()));
            }

            // return the list of AulaDTO objects
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

    /**
     * Retrieves an AulaDTO object by its ID.
     *
     * @param id the ID of the AulaDTO object to retrieve.
     * @return the AulaDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public AulaDTO findAulaById(Long id) throws RestException {

        // Get aula by Id
        Aula aula = aulaRepository.findById(id).orElse(null);

        // Check if exists
        if(aula == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontro el aula",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        // fin bloque by number
        Bloque bloque = bloqueRepository.findByNumero(aula.getBloque().getNumero());

        // convert Aula to DTO enitity
        AulaDTO aulaDTO = aulasConverter.aulaToAulaDTO(aula);

        // set bloque founded to aulaDTO
        aulaDTO.setBloque(bloque.getNumero());

        return aulaDTO;
    }


    /**
     * Retrieves AulaDTO objects by their number.
     *
     * @param numero the number of the AulaDTO objects to retrieve.
     * @return a List of AulaDTO objects.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public List<AulaDTO> findAulaByNumero(Long numero) throws RestException {

        // get aula by number
        List<Aula> aulas = aulaRepository.findByNumero(numero);

        // check if exists
        if(aulas.isEmpty()){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontraron aulas con ese numero",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        // convert Aula to DTO entity
        List<AulaDTO> aulasDto = aulas.stream()
                .map(aula -> aulasConverter.aulaToAulaDTO(aula))
                .toList();

        /*
        * For each aula founded we verify if the bloque exists
        * */
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

            // set bloque founded to aulaDTO
            aulasDto.stream()
                    .filter(aulaDto -> aulaDto.getId().equals(aula.getId()))
                    .forEach(aulaDto -> aulaDto.setBloque(bloque.getNumero()));
        }

        return aulasDto;
    }

    /**
     * Creates a new AulaDTO object.
     *
     * @param aulaDto the AulaDTO object to create.
     * @return the created AulaDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public AulaDTO saveAula(AulaDTO aulaDto) throws RestException {

        // Validate if the aulaDto is null
        if(aulaDto == null){
            throw new InternalServerErrorException(ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    ConstUtil.MESSAGE_ERROR_DATA,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        // Find and validate fi bloque exists
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

        // If aulaDto object has an ID, update the existing Aula object
        if(aulaDto.getId() != null){
            boolean exist = aulaRepository.existsById(aulaDto.getId());
            if(exist){
                Aula aulaActualizada = aulasConverter.aulaDTOToAula(aulaDto);
                aulaActualizada.setBloque(bloqueTemp);
                return aulasConverter.aulaToAulaDTO(aulaRepository.save(aulaActualizada));
            }
        }

        // Validate if aula exists in the bloque before saving
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

        // Create a new Aula object
        Aula newAula = aulasConverter.aulaDTOToAula(aulaDto);
        newAula.setBloque(bloqueTemp);

        // Save the new Aula object
        newAula = aulaRepository.save(newAula);
        
        aulaDto.setId(newAula.getId());


        return aulaDto;
    }

    /**
     * Updates an existing AulaDTO object.
     *
     * @param id the AulaDTO object to update.
     * @return the updated AulaDTO object.
     * @throws RestException if an error occurs during the operation.
     */
    @Override
    public void deleteAula(Long id) throws RestException {

        // find aula by id
        Aula aulaEncontrado = aulaRepository.findById(id).orElse(null);

        // verify if aula exists
        if(aulaEncontrado == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No se encontro el aula",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        // delete aula
        aulaRepository.deleteById(id);

    }
}
