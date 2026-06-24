package com.agenciaviajes.backend.service;

import java.util.List;
import java.util.Optional;

import com.agenciaviajes.backend.model.Factura;

public interface FacturaService {

    List<Factura> listar();

    Optional<Factura> buscarPorId(Integer id);

    Factura guardar(Factura factura);

    Factura actualizar(Integer id, Factura factura);

    void eliminar(Integer id);
}
