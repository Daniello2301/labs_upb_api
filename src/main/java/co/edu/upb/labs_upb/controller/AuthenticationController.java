package co.edu.upb.labs_upb.controller;

import co.edu.upb.labs_upb.dto.UsuarioDTO;
import co.edu.upb.labs_upb.exception.NotFoundException;
import co.edu.upb.labs_upb.model.AuthenticationRequest;
import co.edu.upb.labs_upb.model.AuthenticationResponse;
import co.edu.upb.labs_upb.service.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse>  register(@RequestBody UsuarioDTO usuarioDTO) throws NotFoundException {

        return ResponseEntity.ok().body(authenticationService.register(usuarioDTO));

    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse>  login(@RequestBody AuthenticationRequest authRequest){

        return ResponseEntity.ok().body(authenticationService.login(authRequest));
    }

}
