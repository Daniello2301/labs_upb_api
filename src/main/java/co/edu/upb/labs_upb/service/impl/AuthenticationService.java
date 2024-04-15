package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.auth.JwtService;
import co.edu.upb.labs_upb.converter.UsuarioConverter;
import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.model.AuthenticationRequest;
import co.edu.upb.labs_upb.model.AuthenticationResponse;
import co.edu.upb.labs_upb.model.Rol;
import co.edu.upb.labs_upb.model.Usuario;
import co.edu.upb.labs_upb.repository.IRolRepository;
import co.edu.upb.labs_upb.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final UsuarioConverter usuarioConverter;

    private final IRolRepository rolRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(UsuarioDTO userDTO) throws NotFoundException {

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

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Se convierte el usuario DTO a usuario entity
        Usuario usuario = usuarioConverter.usuarioDtoToUsuario(userDTO);

        usuario.setEnable(true);

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
                return new AuthenticationResponse();
            }
        }

        // guardamos el usuario
        usuarioRepository.save(usuario);

        userDTO.setId(usuario.getId());

        var  jwtToken = jwtService.generateToken(usuario);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse login(AuthenticationRequest authRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        var usuario = usuarioRepository.findByEmail(authRequest.getEmail()).orElseThrow();

        var  jwtToken = jwtService.generateToken(usuario);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
