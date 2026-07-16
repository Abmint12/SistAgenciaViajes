package com.agenciaviajes.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.agenciaviajes.backend.dto.LoginRequest;
import com.agenciaviajes.backend.model.Usuario;
import com.agenciaviajes.backend.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioService.login(
                request.getNombreUsuario(),
                request.getContrasena()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("id", usuario.getId());
        response.put("nombreUsuario", usuario.getNombreUsuario());
        response.put("rol", usuario.getRol());

        return ResponseEntity.ok(response);
    }
}