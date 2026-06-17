package com.agenciaviajes.backend.service;

import java.util.List;

import com.agenciaviajes.backend.dto.ReservaRequest;
import com.agenciaviajes.backend.dto.ReservaResponse;

public interface ReservaService {

    List<ReservaResponse> listar();

    ReservaResponse obtener(Long id);

    ReservaResponse crear(ReservaRequest request);

    ReservaResponse actualizar(Long id, ReservaRequest request);

    void eliminar(Long id);

}