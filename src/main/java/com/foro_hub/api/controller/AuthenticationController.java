package com.foro_hub.api.controller;

import com.foro_hub.api.domain.usuario.AutenticacionUsuarioDTO;
import com.foro_hub.api.domain.usuario.Usuario;
import com.foro_hub.api.infra.seguridad.JWTokenDTO;
import com.foro_hub.api.infra.seguridad.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticarUsuario(
            @RequestBody @Valid AutenticacionUsuarioDTO autenticacionUsuarioDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                autenticacionUsuarioDTO.usuario(),
                autenticacionUsuarioDTO.clave());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var JWToken = tokenService.generarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new JWTokenDTO(JWToken));
    }
}