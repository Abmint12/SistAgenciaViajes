package com.agenciaviajes.backend.service;

import com.agenciaviajes.backend.model.Cliente;

import java.util.List;

public interface ClienteService {

    List<Cliente> listar();

    Cliente buscarPorId(Long id);

    Cliente guardar(Cliente cliente);

    Cliente actualizar(Long id, Cliente cliente);

    void eliminar(Long id);
}