package com.agenciaviajes.backend.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.agenciaviajes.backend.model.Destino;
import com.agenciaviajes.backend.service.DestinoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/destinos")
@CrossOrigin(origins = "*")
public class DestinoController {

    private final DestinoService destinoService;

    public DestinoController(DestinoService destinoService) {
        this.destinoService = destinoService;
    }

    @GetMapping
    public ResponseEntity<List<Destino>> listar() {
        return ResponseEntity.ok(destinoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destino> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(destinoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Destino> guardar(@Valid @RequestBody Destino destino) {
        return new ResponseEntity<>(destinoService.guardar(destino), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destino> actualizar(@PathVariable Integer id,
                                              @Valid @RequestBody Destino destino) {

        return ResponseEntity.ok(destinoService.actualizar(id, destino));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {

        destinoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

}