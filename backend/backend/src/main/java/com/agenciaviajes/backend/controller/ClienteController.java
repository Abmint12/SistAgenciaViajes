package com.agenciaviajes.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agenciaviajes.backend.model.Cliente;
import com.agenciaviajes.backend.service.ClienteService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id){
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Long id, @RequestBody Cliente cliente){
        return service.actualizar(id, cliente);
    }
    
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        service.eliminar(id);
    }        
    
}
