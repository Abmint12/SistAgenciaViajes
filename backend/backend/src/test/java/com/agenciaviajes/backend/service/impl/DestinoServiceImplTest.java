package com.agenciaviajes.backend.service.impl;

import com.agenciaviajes.backend.exception.ResourceNotFoundException;
import com.agenciaviajes.backend.model.Destino;
import com.agenciaviajes.backend.repository.DestinoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DestinoServiceImplTest {

    @Mock
    private DestinoRepository destinoRepository;

    @InjectMocks
    private DestinoServiceImpl destinoService;

    @Test
    void listarDebeRetornarTodosLosDestinos() {

        Destino d1 = crearDestino(
                1,
                "Galápagos",
                "Ecuador",
                "Puerto Ayora",
                "Islas turísticas"
        );

        Destino d2 = crearDestino(
                2,
                "Cusco",
                "Perú",
                "Cusco",
                "Ciudad histórica"
        );

        List<Destino> destinos = List.of(d1, d2);

        when(destinoRepository.findAll()).thenReturn(destinos);

        List<Destino> resultado = destinoService.listar();

        assertEquals(2, resultado.size());

        verify(destinoRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarDestino() {

        Destino destino = crearDestino(
                1,
                "Galápagos",
                "Ecuador",
                "Puerto Ayora",
                "Islas turísticas"
        );

        when(destinoRepository.findById(1))
                .thenReturn(Optional.of(destino));

        Destino resultado = destinoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Galápagos", resultado.getNombre());

        verify(destinoRepository).findById(1);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcion() {

        when(destinoRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> destinoService.buscarPorId(99)
        );
    }

    @Test
    void guardarDebeGuardarDestino() {

        Destino destino = crearDestino(
                null,
                "Baños",
                "Ecuador",
                "Baños",
                "Turismo"
        );

        when(destinoRepository.save(destino))
                .thenReturn(destino);

        Destino resultado = destinoService.guardar(destino);

        assertEquals("Baños", resultado.getNombre());

        verify(destinoRepository).save(destino);
    }

    @Test
    void actualizarDebeActualizarDestino() {

        Destino existente = crearDestino(
                1,
                "Galápagos",
                "Ecuador",
                "Puerto Ayora",
                "Antigua"
        );

        Destino nuevo = crearDestino(
                null,
                "Galápagos Premium",
                "Ecuador",
                "Santa Cruz",
                "Nueva descripción"
        );

        when(destinoRepository.findById(1))
                .thenReturn(Optional.of(existente));

        when(destinoRepository.save(existente))
                .thenReturn(existente);

        Destino resultado = destinoService.actualizar(1, nuevo);

        assertEquals("Galápagos Premium", resultado.getNombre());
        assertEquals("Santa Cruz", resultado.getCiudad());

        verify(destinoRepository).save(existente);
    }

    @Test
    void eliminarDebeEliminarDestino() {

        Destino destino = crearDestino(
                1,
                "Galápagos",
                "Ecuador",
                "Puerto Ayora",
                "Turismo"
        );

        when(destinoRepository.findById(1))
                .thenReturn(Optional.of(destino));

        destinoService.eliminar(1);

        verify(destinoRepository).delete(destino);
    }

    private Destino crearDestino(
            Integer id,
            String nombre,
            String pais,
            String ciudad,
            String descripcion
    ) {

        Destino destino = new Destino();

        destino.setIdDestino(id);
        destino.setNombre(nombre);
        destino.setPais(pais);
        destino.setCiudad(ciudad);
        destino.setDescripcion(descripcion);

        return destino;
    }

}