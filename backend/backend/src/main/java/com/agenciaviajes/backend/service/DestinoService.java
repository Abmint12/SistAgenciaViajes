package com.agenciaviajes.backend.service;

import java.util.List;

import com.agenciaviajes.backend.model.Destino;

public interface DestinoService {

    List<Destino> listar();

    Destino buscarPorId(Integer id);

    Destino guardar(Destino destino);

    Destino actualizar(Integer id, Destino destino);

    void eliminar(Integer id);
}
