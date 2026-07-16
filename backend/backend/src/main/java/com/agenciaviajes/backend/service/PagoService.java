package com.agenciaviajes.backend.service;

import java.util.List;

import com.agenciaviajes.backend.model.Pago;

public interface PagoService {

    Pago crear(Pago pago);

    List<Pago> listar();

    List<Pago> listarPagados();

    Pago buscarPorId(Long id);

    Pago actualizar(Long id, Pago pago);

    void eliminar(Long id);
}