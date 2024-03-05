package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.converter.UsuarioConverter;
import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Rol;
import co.edu.upb.labs_upb.model.Usuario;
import co.edu.upb.labs_upb.repository.IRolRepository;
import co.edu.upb.labs_upb.repository.IUsuarioRepository;
import co.edu.upb.labs_upb.service.iface.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository  usuarioRepository;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private UsuarioConverter usuarioConverter;

    @Override
    public Page<Usuario> usersPagination(int numPage, int sizePage) throws RestException {
        return null;
    }

    @Override
    public Page<Usuario> usersPaginationSortBy(String sortBy) throws RestException {
        return null;
    }

    @Override
    public Page<Usuario> usersPaginationSortBy(int numPage, int sizePage, String sortBy) throws RestException {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAllUsers() throws RestException {

        List<Usuario> usuarios = usuarioRepository.findAll();

        if(usuarios.isEmpty()){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No Se encontraron usuarios",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        List<UsuarioDTO> usuariosDTO = usuarios.stream().map(usuario -> usuarioConverter.usuarioToUsurioDTO(usuario)).collect(Collectors.toList());

        for(Usuario usuario : usuarios){
            for(UsuarioDTO usuarioDTO : usuariosDTO){
                if(usuario.getId().equals(usuarioDTO.getId())){
                    usuarioDTO.setRoles(usuario.getRoles().stream().map(rol -> rol.getNombre()).collect(Collectors.toSet()));
                }
            }

        }

        return usuariosDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO findByCorreo(String email) throws RestException {

        Usuario usuarioEncontrado = usuarioRepository.findByCorreo(email);

        UsuarioDTO usuarioDTO = usuarioConverter.usuarioToUsurioDTO(usuarioEncontrado);

        usuarioDTO.setRoles(usuarioEncontrado
                .getRoles()
                .stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet()));

        return usuarioDTO;
    }

    @Override
    public UsuarioDTO findUserById(Long id) throws RestException {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if(usuario == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No Se encontraron usuarios",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        UsuarioDTO usuarioDTO = usuarioConverter.usuarioToUsurioDTO(usuario);

        usuarioDTO.setRoles(usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet()));

        return usuarioDTO;
    }


    @Override
    @Transactional
    public UsuarioDTO saveUser(UsuarioDTO userDTO) throws RestException {

        // Validamos que si se pase un usuaro en la peticion
        if(userDTO == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "we can't save a null user",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        // Se busca usuario por documento si no e
        Usuario existByDocument = usuarioRepository.findByDocumento(userDTO.getDocumento());
        // Se confirma que no exista un usuario con el mismo documento
        if((existByDocument != null) && (userDTO.getId() == null)){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.ALREADY_REPORTED.getReasonPhrase(),
                            "The user already exist",
                            HttpStatus.ALREADY_REPORTED.value()
                    )
            );
        }

        // Se convierte el usuario DTO a usuario entity
        Usuario usuario = usuarioConverter.usuarioDtoToUsuario(userDTO);

        // Se mapean los roles de la lista de string del usuario DTO a una lista tipo Rol para el usuario entity
        Set<Rol> roles = new HashSet<>();
        for(String rol : userDTO.getRoles()) {
            Rol rol1 = rolRepository.findByNombre(rol);
            if (rol1 == null) {
                throw new NotFoundException(
                        ErrorDto.getErrorDto(
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                "The role not exist",
                                HttpStatus.NOT_FOUND.value()
                        )
                );
            }
            roles.add(rol1);
        }
        // agregamos los roles convertidos al usuario
        usuario.setRoles(roles);


        // si el usuario entrante tiene un id, se actualiza el usuario
        if(userDTO.getId() != null){
            boolean exist = usuarioRepository.existsById(userDTO.getId());
            if(exist)
            {
                return usuarioConverter.usuarioToUsurioDTO(usuarioRepository.save(usuario));
            }
        }

        // guardamos el usuario
        usuarioRepository.save(usuario);

        userDTO.setId(usuario.getId());

        return userDTO;
    }

    @Override
    public void deleteUser(Long id) throws RestException {
        try{

            Usuario usuarioDtoEncontrado = usuarioRepository.findById(id).orElse(null);

        }catch (Exception e){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "The user not exist",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }


    }
}
