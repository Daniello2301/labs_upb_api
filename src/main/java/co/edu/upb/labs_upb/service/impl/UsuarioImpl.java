package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.converter.UsuarioConverter;
import co.edu.upb.labs_upb.dto.ChangePasswordReset;
import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.BadRequestException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioImpl implements UserDetailsService, IUsuarioService {

    @Autowired
    private IUsuarioRepository  usuarioRepository;

    @Autowired
    private IRolRepository rolRepository;

    @Autowired
    private UsuarioConverter usuarioConverter;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Method for paginating users.
     */
    @Override
    public Page<Usuario> usersPagination(int numPage, int sizePage) throws RestException {
        return null;
    }


    /**
     * Method for paginating users with sorting.
     */
    @Override
    public Page<Usuario> usersPaginationSortBy(String sortBy) throws RestException {
        return null;
    }


    /**
     * Method for paginating users with sorting and page size.
     */
    @Override
    public Page<Usuario> usersPaginationSortBy(int numPage, int sizePage, String sortBy) throws RestException {
        return null;
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByEmail(username);
    }

    /**
     * Method for finding all users.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAllUsers() throws RestException {

        List<Usuario> usuarios = usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "No Se encontraron usuarios",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        List<UsuarioDTO> usuariosDTO = usuarios.stream().map(usuario -> usuarioConverter.usuarioToUsurioDTO(usuario)).collect(Collectors.toList());

        for (Usuario usuario : usuarios) {
            for (UsuarioDTO usuarioDTO : usuariosDTO) {
                if (usuario.getId().equals(usuarioDTO.getId())) {
                    usuarioDTO.setRoles(usuario.getRoles().stream().map(rol -> rol.getNombre()).collect(Collectors.toSet()));
                }
            }

        }

        return usuariosDTO;
    }

    /**
     * Method for finding a user by email.
     */
    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO findByCorreo(String email) throws RestException {

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(email);

        UsuarioDTO usuarioDTO = usuarioConverter.usuarioToUsurioDTO(usuarioEncontrado.get());

        usuarioDTO.setRoles(usuarioEncontrado
                .get()
                .getRoles()
                .stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet()));

        return usuarioDTO;
    }

    /**
     * Method for finding a user by id.
     */
    @Override
    public UsuarioDTO findUserById(Long id) throws RestException {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null) {
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


    /**
     * Method for saving a user.
     */
    @Override
    @Transactional
    public UsuarioDTO saveUser(UsuarioDTO userDTO) throws RestException {

        // Validamos que si se pase un usuaro en la peticion
        if (userDTO == null) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "we can't save a null user",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }

        // Se busca usuario por documento si no existe
        Usuario existByDocument = usuarioRepository.findByDocumento(userDTO.getDocumento());
        // Se confirma que no exista un usuario con el mismo documento
        if ((existByDocument != null) && (userDTO.getId() == null)) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.ALREADY_REPORTED.getReasonPhrase(),
                            "The user already exist",
                            HttpStatus.ALREADY_REPORTED.value()
                    )
            );
        }

        // Se convierte el usuario DTO a usuario entity
        var usuario = usuarioConverter.usuarioDtoToUsuario(userDTO);

        usuario.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Se mapean los roles de la lista de string del usuario DTO a una lista tipo Rol para el usuario entity
        Set<Rol> roles = new HashSet<>();
        for (String rol : userDTO.getRoles()) {
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
        usuario.setEnable(true);

        // guardamos el usuario
        var userUpdated = usuarioRepository.save(usuario);

        userDTO.setId(userUpdated.getId());
        userDTO.setPassword(userUpdated.getPassword());


        return userDTO;
    }

    @Override
    public void changePassword(ChangePasswordReset request, Authentication connectedUser) throws RestException {

            var usuario = (Usuario) connectedUser.getPrincipal();

            if (!passwordEncoder.matches(request.getCurrentPassword(), usuario.getPassword())) {
                throw new BadRequestException(
                        ErrorDto.getErrorDto(
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "The password not match",
                                HttpStatus.BAD_REQUEST.value()
                        )
                );
            }

            if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                throw new BadRequestException(
                        ErrorDto.getErrorDto(
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "bad request",
                                HttpStatus.BAD_REQUEST.value()
                        )
                );
            }

            usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));
            usuario.setFechaActualizacion(LocalDateTime.now());

            usuarioRepository.save(usuario);
    }

    /**
     * Method for deleting a user.
     */
    @Override
    public void deleteUser(Long id) throws RestException {
        try {

            Usuario usuarioDtoEncontrado = usuarioRepository.findById(id).orElse(null);

        } catch (Exception e) {
            throw new NotFoundException(
                    ErrorDto.getErrorDto(
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            "The user not exist",
                            HttpStatus.NOT_FOUND.value()
                    )
            );
        }


    }



    /**
     * Method for loading a user by username.
     */
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Optional<Usuario> usuario = usuarioRepository.findByEmail(correo);
        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Error in login with credential " + correo);
        }

        return new User(
                usuario.get().getUsername(),
                usuario.get().getPassword(),
                usuario.get().getEnable(),
true,
true,
true,
                usuario.get().getAuthorities());
    }
}
