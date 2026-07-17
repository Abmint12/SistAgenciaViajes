package com.agenciaviajes.backend.service.impl;

import com.agenciaviajes.backend.dto.ReservaRequest;
import com.agenciaviajes.backend.dto.ReservaResponse;
import com.agenciaviajes.backend.model.Cliente;
import com.agenciaviajes.backend.model.Destino;
import com.agenciaviajes.backend.model.Reserva;
import com.agenciaviajes.backend.repository.ClienteRepository;
import com.agenciaviajes.backend.repository.DestinoRepository;
import com.agenciaviajes.backend.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private DestinoRepository destinoRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Test
    void listarDebeRetornarTodasLasReservas() {
        Cliente cliente = crearCliente(
                1L,
                "Lady",
                "Zhinin"
        );

        Destino destino = crearDestino(
                1,
                "Galápagos"
        );

        Reserva reserva1 = crearReserva(
                1L,
                cliente,
                destino,
                LocalDate.of(2026, 8, 10),
                2,
                new BigDecimal("500.00"),
                "CONFIRMADA"
        );

        Reserva reserva2 = crearReserva(
                2L,
                cliente,
                destino,
                LocalDate.of(2026, 9, 15),
                3,
                new BigDecimal("750.00"),
                "PENDIENTE"
        );

        when(reservaRepository.findAll())
                .thenReturn(List.of(reserva1, reserva2));

        List<ReservaResponse> resultado = reservaService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Lady Zhinin", resultado.get(0).getNombreCliente());
        assertEquals("Galápagos", resultado.get(0).getNombreDestino());

        assertEquals(2L, resultado.get(1).getId());
        assertEquals("PENDIENTE", resultado.get(1).getEstado());

        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    void obtenerDebeRetornarReservaCuandoExiste() {
        Cliente cliente = crearCliente(
                1L,
                "Lady",
                "Zhinin"
        );

        Destino destino = crearDestino(
                1,
                "Galápagos"
        );

        Reserva reserva = crearReserva(
                1L,
                cliente,
                destino,
                LocalDate.of(2026, 8, 10),
                2,
                new BigDecimal("500.00"),
                "CONFIRMADA"
        );

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        ReservaResponse resultado = reservaService.obtener(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getIdCliente());
        assertEquals("Lady Zhinin", resultado.getNombreCliente());
        assertEquals(1, resultado.getIdDestino());
        assertEquals("Galápagos", resultado.getNombreDestino());
        assertEquals(2, resultado.getCantPasajes());
        assertEquals(
                new BigDecimal("500.00"),
                resultado.getCostoTotal()
        );
        assertEquals("CONFIRMADA", resultado.getEstado());

        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerDebeLanzarExcepcionCuandoReservaNoExiste() {
        when(reservaRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> reservaService.obtener(99L)
        );

        assertEquals(
                "Reserva no encontrada",
                excepcion.getMessage()
        );

        verify(reservaRepository, times(1)).findById(99L);
    }

    @Test
    void crearDebeGuardarReservaValida() {
        Cliente cliente = crearCliente(
                1L,
                "Lady",
                "Zhinin"
        );

        Destino destino = crearDestino(
                1,
                "Galápagos"
        );

        ReservaRequest request = crearRequest(
                1L,
                1,
                LocalDate.of(2026, 8, 10),
                2,
                new BigDecimal("500.00"),
                "CONFIRMADA"
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(destinoRepository.findById(1))
                .thenReturn(Optional.of(destino));

        when(reservaRepository.save(any(Reserva.class)))
                .thenAnswer(invocacion -> {
                    Reserva reservaGuardada = invocacion.getArgument(0);
                    reservaGuardada.setId(1L);
                    return reservaGuardada;
                });

        ReservaResponse resultado = reservaService.crear(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getIdCliente());
        assertEquals("Lady Zhinin", resultado.getNombreCliente());
        assertEquals(1, resultado.getIdDestino());
        assertEquals("Galápagos", resultado.getNombreDestino());
        assertEquals(
                LocalDate.of(2026, 8, 10),
                resultado.getFechaViaje()
        );
        assertEquals(2, resultado.getCantPasajes());
        assertEquals(
                new BigDecimal("500.00"),
                resultado.getCostoTotal()
        );
        assertEquals("CONFIRMADA", resultado.getEstado());

        verify(clienteRepository, times(1)).findById(1L);
        verify(destinoRepository, times(1)).findById(1);
        verify(reservaRepository, times(1))
                .save(any(Reserva.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoClienteNoExiste() {
        ReservaRequest request = crearRequest(
                99L,
                1,
                LocalDate.of(2026, 8, 10),
                2,
                new BigDecimal("500.00"),
                "CONFIRMADA"
        );

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> reservaService.crear(request)
        );

        assertEquals(
                "Cliente no encontrado",
                excepcion.getMessage()
        );

        verify(clienteRepository, times(1)).findById(99L);
        verifyNoInteractions(destinoRepository);
        verify(reservaRepository, never())
                .save(any(Reserva.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoDestinoNoExiste() {
        Cliente cliente = crearCliente(
                1L,
                "Lady",
                "Zhinin"
        );

        ReservaRequest request = crearRequest(
                1L,
                99,
                LocalDate.of(2026, 8, 10),
                2,
                new BigDecimal("500.00"),
                "CONFIRMADA"
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(destinoRepository.findById(99))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> reservaService.crear(request)
        );

        assertEquals(
                "Destino no encontrado",
                excepcion.getMessage()
        );

        verify(clienteRepository, times(1)).findById(1L);
        verify(destinoRepository, times(1)).findById(99);
        verify(reservaRepository, never())
                .save(any(Reserva.class));
    }

    @Test
    void actualizarDebeModificarReservaValida() {
        Cliente clienteAnterior = crearCliente(
                1L,
                "Lady",
                "Zhinin"
        );

        Destino destinoAnterior = crearDestino(
                1,
                "Galápagos"
        );

        Reserva reservaExistente = crearReserva(
                1L,
                clienteAnterior,
                destinoAnterior,
                LocalDate.of(2026, 8, 10),
                2,
                new BigDecimal("500.00"),
                "PENDIENTE"
        );

        Cliente nuevoCliente = crearCliente(
                2L,
                "Lisette",
                "Zhinin"
        );

        Destino nuevoDestino = crearDestino(
                2,
                "Cusco"
        );

        ReservaRequest request = crearRequest(
                2L,
                2,
                LocalDate.of(2026, 9, 20),
                4,
                new BigDecimal("900.00"),
                "CONFIRMADA"
        );

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reservaExistente));

        when(clienteRepository.findById(2L))
                .thenReturn(Optional.of(nuevoCliente));

        when(destinoRepository.findById(2))
                .thenReturn(Optional.of(nuevoDestino));

        when(reservaRepository.save(reservaExistente))
                .thenReturn(reservaExistente);

        ReservaResponse resultado = reservaService.actualizar(
                1L,
                request
        );

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(2L, resultado.getIdCliente());
        assertEquals(
                "Lisette Zhinin",
                resultado.getNombreCliente()
        );
        assertEquals(2, resultado.getIdDestino());
        assertEquals("Cusco", resultado.getNombreDestino());
        assertEquals(
                LocalDate.of(2026, 9, 20),
                resultado.getFechaViaje()
        );
        assertEquals(4, resultado.getCantPasajes());
        assertEquals(
                new BigDecimal("900.00"),
                resultado.getCostoTotal()
        );
        assertEquals("CONFIRMADA", resultado.getEstado());

        verify(reservaRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).findById(2L);
        verify(destinoRepository, times(1)).findById(2);
        verify(reservaRepository, times(1))
                .save(reservaExistente);
    }

    @Test
    void actualizarDebeLanzarExcepcionCuandoReservaNoExiste() {
        ReservaRequest request = crearRequest(
                1L,
                1,
                LocalDate.of(2026, 8, 10),
                2,
                new BigDecimal("500.00"),
                "CONFIRMADA"
        );

        when(reservaRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> reservaService.actualizar(
                        99L,
                        request
                )
        );

        assertEquals(
                "Reserva no encontrada",
                excepcion.getMessage()
        );

        verify(reservaRepository, times(1)).findById(99L);
        verifyNoInteractions(clienteRepository);
        verifyNoInteractions(destinoRepository);
        verify(reservaRepository, never())
                .save(any(Reserva.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionCuandoClienteNoExiste() {
        Cliente clienteAnterior = crearCliente(
                1L,
                "Lady",
                "Zhinin"
        );

        Destino destinoAnterior = crearDestino(
                1,
                "Galápagos"
        );

        Reserva reservaExistente = crearReserva(
                1L,
                clienteAnterior,
                destinoAnterior,
                LocalDate.of(2026, 8, 10),
                2,
                new BigDecimal("500.00"),
                "PENDIENTE"
        );

        ReservaRequest request = crearRequest(
                99L,
                1,
                LocalDate.of(2026, 9, 20),
                4,
                new BigDecimal("900.00"),
                "CONFIRMADA"
        );

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reservaExistente));

        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> reservaService.actualizar(
                        1L,
                        request
                )
        );

        assertEquals(
                "Cliente no encontrado",
                excepcion.getMessage()
        );

        verify(reservaRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).findById(99L);
        verifyNoInteractions(destinoRepository);
        verify(reservaRepository, never())
                .save(any(Reserva.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionCuandoDestinoNoExiste() {
        Cliente clienteAnterior = crearCliente(
                1L,
                "Lady",
                "Zhinin"
        );

        Destino destinoAnterior = crearDestino(
                1,
                "Galápagos"
        );

        Reserva reservaExistente = crearReserva(
                1L,
                clienteAnterior,
                destinoAnterior,
                LocalDate.of(2026, 8, 10),
                2,
                new BigDecimal("500.00"),
                "PENDIENTE"
        );

        Cliente nuevoCliente = crearCliente(
                2L,
                "Lisette",
                "Zhinin"
        );

        ReservaRequest request = crearRequest(
                2L,
                99,
                LocalDate.of(2026, 9, 20),
                4,
                new BigDecimal("900.00"),
                "CONFIRMADA"
        );

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reservaExistente));

        when(clienteRepository.findById(2L))
                .thenReturn(Optional.of(nuevoCliente));

        when(destinoRepository.findById(99))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> reservaService.actualizar(
                        1L,
                        request
                )
        );

        assertEquals(
                "Destino no encontrado",
                excepcion.getMessage()
        );

        verify(reservaRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).findById(2L);
        verify(destinoRepository, times(1)).findById(99);
        verify(reservaRepository, never())
                .save(any(Reserva.class));
    }

    @Test
    void eliminarDebeEliminarReservaCuandoExiste() {
        Cliente cliente = crearCliente(
                1L,
                "Lady",
                "Zhinin"
        );

        Destino destino = crearDestino(
                1,
                "Galápagos"
        );

        Reserva reserva = crearReserva(
                1L,
                cliente,
                destino,
                LocalDate.of(2026, 8, 10),
                2,
                new BigDecimal("500.00"),
                "CONFIRMADA"
        );

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        reservaService.eliminar(1L);

        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).delete(reserva);
    }

    @Test
    void eliminarDebeLanzarExcepcionCuandoReservaNoExiste() {
        when(reservaRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> reservaService.eliminar(99L)
        );

        assertEquals(
                "Reserva no encontrada",
                excepcion.getMessage()
        );

        verify(reservaRepository, times(1)).findById(99L);
        verify(reservaRepository, never())
                .delete(any(Reserva.class));
    }

    private ReservaRequest crearRequest(
            Long idCliente,
            Integer idDestino,
            LocalDate fechaViaje,
            Integer cantPasajes,
            BigDecimal costoTotal,
            String estado
    ) {
        ReservaRequest request = new ReservaRequest();

        request.setIdCliente(idCliente);
        request.setIdDestino(idDestino);
        request.setFechaViaje(fechaViaje);
        request.setCantPasajes(cantPasajes);
        request.setCostoTotal(costoTotal);
        request.setEstado(estado);

        return request;
    }

    private Reserva crearReserva(
            Long id,
            Cliente cliente,
            Destino destino,
            LocalDate fechaViaje,
            Integer cantPasajes,
            BigDecimal costoTotal,
            String estado
    ) {
        Reserva reserva = new Reserva();

        reserva.setId(id);
        reserva.setCliente(cliente);
        reserva.setDestino(destino);
        reserva.setFechaViaje(fechaViaje);
        reserva.setCantPasajes(cantPasajes);
        reserva.setCostoTotal(costoTotal);
        reserva.setEstado(estado);

        return reserva;
    }

    private Cliente crearCliente(
            Long id,
            String nombre,
            String apellido
    ) {
        Cliente cliente = new Cliente();

        cliente.setId(id);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);

        return cliente;
    }

    private Destino crearDestino(
            Integer id,
            String nombre
    ) {
        Destino destino = new Destino();

        destino.setIdDestino(id);
        destino.setNombre(nombre);

        return destino;
    }
}