package com.agenciaviajes.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agenciaviajes.backend.model.Cliente;
import com.agenciaviajes.backend.service.ClienteService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService service;
    
    public ClienteController(ClienteService service){
        this.service=service;
    }
    @GetMapping
    public List<Cliente> listar(){
        return service.listar();
    }
    
    @PostMapping Cliente guardar(@RequestBody Cliente cliente){
        return service.guardar(cliente);
    }
    
    
    
}
