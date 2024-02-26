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

        usuarioDTO.setRoles(usuario.getRoles().stream().map(rol -> rol.getNombre()).collect(Collectors.toSet()));

        return usuarioDTO;
    }

    @Override
    public UsuarioDTO saveUser(UsuarioDTO user) throws RestException {

        if(user == null){
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "we can't save a null user",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        LocalDateTime now = LocalDateTime.now();

        user.setFechaCreacion(now);
        user.setFechaActualizacion(now);

        Usuario usuario = UsuarioConverter.usuarioDtoToUsuario(user);
        if(user.getId() != null){
            Usuario existByDocument = usuarioRepository.findByDocumento(user.getDocumento());
            Usuario existById = usuarioRepository.findById(user.getId()).orElse(null);
            if((existByDocument != null) || (existById != null)){
                throw new NotFoundException(
                        ErrorDto.getErrorDto(
                                HttpStatus.ALREADY_REPORTED.getReasonPhrase(),
                                "The user already exist",
                                HttpStatus.ALREADY_REPORTED.value()
                        )
                );
            }
        }

        Set<Rol> roles = new HashSet<>();
        for(String rol : user.getRoles()){
            Rol rol1 = rolRepository.findByNombre(rol);
            if(rol1 == null){
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

        usuario.setRoles(roles);


        usuarioRepository.save(usuario);

        user.setId(usuario.getId());

        return user;
    }

    @Override
    public UsuarioDTO updateUser(Long id, UsuarioDTO user) throws RestException {
        return null;
    }

    @Override
    public void deleteUser(Long id) throws RestException {

    }
}
