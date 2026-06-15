package com.agenciaviajes.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.agenciaviajes.backend.model.Cliente;
import com.agenciaviajes.backend.repository.ClienteRepository;

@Service
public class ClienteService {
    private final ClienteRepository repository;
    public ClienteService(ClienteRepository repository){
        this.repository=repository;
    }
public List<Cliente>listar(){
    return repository.findAll();
    }
public Cliente guardar(Cliente cliente){
    
    //validacion de cedula backend
    if(cliente.getCedula().length()!=10){
        throw new RuntimeException("Cedula inválida");
    }
    return repository.save(cliente);
}
}
