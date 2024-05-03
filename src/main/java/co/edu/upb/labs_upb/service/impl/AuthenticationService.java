package co.edu.upb.labs_upb.service.impl;

import co.edu.upb.labs_upb.auth.JwtService;
import co.edu.upb.labs_upb.converter.UsuarioConverter;
import co.edu.upb.labs_upb.dto.AuthenticationRequest;
import co.edu.upb.labs_upb.dto.AuthenticationResponse;
import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.model.Rol;
import co.edu.upb.labs_upb.model.Token;
import co.edu.upb.labs_upb.model.Usuario;
import co.edu.upb.labs_upb.model.TokenType;
import co.edu.upb.labs_upb.repository.IRolRepository;
import co.edu.upb.labs_upb.repository.IUsuarioRepository;
import co.edu.upb.labs_upb.service.iface.ITokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements LogoutHandler {


    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioConverter usuarioConverter;
    @Autowired
    private IRolRepository rolRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private final ITokenRepository tokenRepository;

    @Transactional
    public AuthenticationResponse register(UsuarioDTO userDTO) throws RestException {

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

        // Se busca usuario por documento si no e
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
        Usuario usuario = usuarioConverter.usuarioDtoToUsuario(userDTO);

        usuario.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        usuario.setEnable(true);

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

        // guardamos el usuario

        var  jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);

        var usuarioguardado = usuarioRepository.save(usuario);

        saveUserToken(usuarioguardado, jwtToken);

        userDTO.setId(usuario.getId());


        System.out.println(userDTO + " " + jwtToken);

        return AuthenticationResponse.builder()
                .id(usuario.getIdUpb())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }


    @Transactional
    public AuthenticationResponse login(AuthenticationRequest authRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        var usuario = usuarioRepository.findByEmail(authRequest.getEmail()).orElseThrow();


        var  jwtToken = jwtService.generateToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);

        revokeAllUserTokens(usuario);
        saveUserToken(usuario, jwtToken);

        return AuthenticationResponse.builder()
                .id(usuario.getIdUpb())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final int startIndex = 7;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(startIndex);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }

    private void saveUserToken(Usuario user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Usuario user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        final int startIndex = 7;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(startIndex);
        userEmail = jwtService.extractUserEmail(refreshToken);
        if (userEmail != null) {

            var user = usuarioRepository.findByEmail(userEmail).orElseThrow();

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
