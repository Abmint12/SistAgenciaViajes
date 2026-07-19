package com.agenciaviajes.backend.service.impl;

import com.agenciaviajes.backend.exception.ResourceNotFoundException;
import com.agenciaviajes.backend.model.Cliente;
import com.agenciaviajes.backend.repository.ClienteRepository;
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
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void listarDebeRetornarTodosLosClientes() {
        Cliente cliente1 = crearCliente(
                1L,
                "Lady",
                "Zhinin",
                "0943882423",
                "0985607616",
                "lady@gmail.com",
                "Guayaquil"
        );

        Cliente cliente2 = crearCliente(
                2L,
                "Carlos",
                "Pérez",
                "1234567890",
                "0999999999",
                "carlos@gmail.com",
                "Durán"
        );

        List<Cliente> clientesEsperados = List.of(cliente1, cliente2);

        when(clienteRepository.findAll()).thenReturn(clientesEsperados);

        List<Cliente> resultado = clienteService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(clientesEsperados, resultado);

        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarClienteCuandoExiste() {
        Cliente cliente = crearCliente(
                1L,
                "Lady",
                "Zhinin",
                "0943882423",
                "0985607616",
                "lady@gmail.com",
                "Guayaquil"
        );

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Lady", resultado.getNombre());
        assertEquals("Zhinin", resultado.getApellido());

        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> clienteService.buscarPorId(99L)
        );

        assertEquals(
                "Cliente no encontrado con id: 99",
                excepcion.getMessage()
        );

        verify(clienteRepository, times(1)).findById(99L);
    }

    @Test
    void guardarDebeRetornarClienteGuardado() {
        Cliente clienteSinId = crearCliente(
                null,
                "Ana",
                "López",
                "0987654321",
                "0981111111",
                "ana@gmail.com",
                "Quito"
        );

        Cliente clienteGuardado = crearCliente(
                3L,
                "Ana",
                "López",
                "0987654321",
                "0981111111",
                "ana@gmail.com",
                "Quito"
        );

        when(clienteRepository.save(clienteSinId)).thenReturn(clienteGuardado);

        Cliente resultado = clienteService.guardar(clienteSinId);

        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Ana", resultado.getNombre());
        assertEquals("López", resultado.getApellido());

        verify(clienteRepository, times(1)).save(clienteSinId);
    }

    @Test
    void actualizarDebeModificarLosDatosDelCliente() {
        Cliente clienteExistente = crearCliente(
                1L,
                "Lady",
                "Zhinin",
                "0943882423",
                "0985607616",
                "lady@gmail.com",
                "Guayaquil"
        );

        Cliente datosActualizados = crearCliente(
                null,
                "Lady María",
                "Zhinin Gómez",
                "0000000000",
                "0999999999",
                "ladynueva@gmail.com",
                "Cuenca"
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(clienteExistente));

        when(clienteRepository.save(clienteExistente))
                .thenReturn(clienteExistente);

        Cliente resultado = clienteService.actualizar(
                1L,
                datosActualizados
        );

        assertNotNull(resultado);
        assertEquals("Lady María", resultado.getNombre());
        assertEquals("Zhinin Gómez", resultado.getApellido());
        assertEquals("0999999999", resultado.getTelefono());
        assertEquals("ladynueva@gmail.com", resultado.getCorreo());

        assertEquals("0943882423", resultado.getCedula());
        assertEquals("Guayaquil", resultado.getDireccion());

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(clienteExistente);
    }

    @Test
    void actualizarDebeLanzarExcepcionCuandoClienteNoExiste() {
        Cliente datosActualizados = crearCliente(
                null,
                "Nombre nuevo",
                "Apellido nuevo",
                "0000000000",
                "0999999999",
                "nuevo@gmail.com",
                "Cuenca"
        );

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> clienteService.actualizar(
                        99L,
                        datosActualizados
                )
        );

        assertEquals(
                "Cliente no encontrado con id: 99",
                excepcion.getMessage()
        );

        verify(clienteRepository, times(1)).findById(99L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void eliminarDebeEliminarClienteCuandoExiste() {
        Cliente cliente = crearCliente(
                1L,
                "Lady",
                "Zhinin",
                "0943882423",
                "0985607616",
                "lady@gmail.com",
                "Guayaquil"
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        clienteService.eliminar(1L);

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    void eliminarDebeLanzarExcepcionCuandoClienteNoExiste() {
        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> clienteService.eliminar(99L)
        );

        assertEquals(
                "Cliente no encontrado con id: 99",
                excepcion.getMessage()
        );

        verify(clienteRepository, times(1)).findById(99L);
        verify(clienteRepository, never()).delete(any(Cliente.class));
    }

    private Cliente crearCliente(
            Long id,
            String nombre,
            String apellido,
            String cedula,
            String telefono,
            String correo,
            String direccion
    ) {
        return new Cliente(
                id,
                nombre,
                apellido,
                cedula,
                telefono,
                correo,
                direccion
        );
    }
}