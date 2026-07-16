package com.agenciaviajes.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.agenciaviajes.backend.model.Usuario;
import com.agenciaviajes.backend.service.UsuarioService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "+")

public class UsuarioController {

    private final UsuarioService usuarioService;
    
    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService=usuarioService;
    }

    @GetMapping
    public ResponseEntity <List<Usuario>> listar(){
        return ResponseEntity.ok(usuarioService.listar());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario>buscarPorId(@PathVariable Integer id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
    
    @PostMapping 
    public ResponseEntity<Usuario> guardar (@Valid @RequestBody Usuario usuario){
        return new ResponseEntity<>(usuarioService.guardar(usuario), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Integer id,@Valid @RequestBody Usuario usuario) {     
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}
