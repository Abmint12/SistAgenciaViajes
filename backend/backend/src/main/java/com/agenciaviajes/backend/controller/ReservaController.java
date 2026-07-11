package com.agenciaviajes.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.agenciaviajes.backend.dto.ReservaRequest;
import com.agenciaviajes.backend.dto.ReservaResponse;
import com.agenciaviajes.backend.model.Reserva;
import com.agenciaviajes.backend.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public List<ReservaResponse> listar() {
        return reservaService.listar();
    }

    @GetMapping("/{id}")
    public ReservaResponse obtener(@PathVariable Long id) {
        return reservaService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaResponse crear(@RequestBody ReservaRequest request) {
        return reservaService.crear(request);
    }

    @PutMapping("/{id}")
    public ReservaResponse actualizar(
            @PathVariable Long id,
            @RequestBody ReservaRequest request) {

        return reservaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
    }

    

}