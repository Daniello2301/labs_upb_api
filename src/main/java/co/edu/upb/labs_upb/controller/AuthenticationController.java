package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.BadRequestException;
import co.edu.upb.labs_upb.exception.RestException;
import co.edu.upb.labs_upb.dto.AuthenticationRequest;
import co.edu.upb.labs_upb.dto.AuthenticationResponse;
import co.edu.upb.labs_upb.service.impl.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object>  register(@RequestBody UsuarioDTO usuarioDTO) throws RestException {
        try {
            LocalDateTime now = LocalDateTime.now();
            usuarioDTO.setFechaCreacion(now);

            return ResponseEntity.ok().body(authenticationService.register(usuarioDTO));
        } catch (BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrorDto());
        }

    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse>  login(@RequestBody AuthenticationRequest authRequest) {

        return ResponseEntity.ok().body(authenticationService.login(authRequest));
    }


    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

}
