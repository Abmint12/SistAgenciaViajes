package com.agenciaviajes.backend.service.impl;

import com.agenciaviajes.backend.enums.EstadoPago;
import com.agenciaviajes.backend.model.Pago;
import com.agenciaviajes.backend.model.Reserva;
import com.agenciaviajes.backend.repository.PagoRepository;
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
class PagoServiceImplTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    @Test
    void crearDebeGuardarPagoValido() {
        Reserva reservaSeleccionada = crearReserva(1L);
        Reserva reservaEncontrada = crearReserva(1L);

        Pago pago = crearPago(
                null,
                reservaSeleccionada,
                new BigDecimal("150.00"),
                "EFECTIVO",
                null
        );

        Pago pagoGuardado = crearPago(
                1L,
                reservaEncontrada,
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reservaEncontrada));

        when(pagoRepository.existsByReservaId(1L))
                .thenReturn(false);

        when(pagoRepository.save(pago))
                .thenReturn(pagoGuardado);

        Pago resultado = pagoService.crear(pago);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(new BigDecimal("150.00"), resultado.getMonto());
        assertEquals("EFECTIVO", resultado.getMetodoPago());
        assertEquals(EstadoPago.PENDIENTE, resultado.getEstado());
        assertEquals(reservaEncontrada, pago.getReserva());

        verify(reservaRepository, times(1)).findById(1L);
        verify(pagoRepository, times(1)).existsByReservaId(1L);
        verify(pagoRepository, times(1)).save(pago);
    }

    @Test
    void crearDebeLanzarExcepcionCuandoNoSeleccionaReserva() {
        Pago pago = crearPago(
                null,
                null,
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals(
                "Debe seleccionar una reserva.",
                excepcion.getMessage()
        );

        verifyNoInteractions(reservaRepository);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoReservaNoTieneId() {
        Reserva reservaSinId = crearReserva(null);

        Pago pago = crearPago(
                null,
                reservaSinId,
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals(
                "Debe seleccionar una reserva.",
                excepcion.getMessage()
        );

        verifyNoInteractions(reservaRepository);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoReservaNoExiste() {
        Reserva reserva = crearReserva(99L);

        Pago pago = crearPago(
                null,
                reserva,
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        when(reservaRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals(
                "La reserva no existe.",
                excepcion.getMessage()
        );

        verify(reservaRepository, times(1)).findById(99L);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoReservaYaTienePago() {
        Reserva reserva = crearReserva(1L);

        Pago pago = crearPago(
                null,
                reserva,
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        when(pagoRepository.existsByReservaId(1L))
                .thenReturn(true);

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals(
                "La reserva ya tiene un pago registrado.",
                excepcion.getMessage()
        );

        verify(reservaRepository, times(1)).findById(1L);
        verify(pagoRepository, times(1)).existsByReservaId(1L);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoMontoEsNulo() {
        Reserva reserva = crearReserva(1L);

        Pago pago = crearPago(
                null,
                reserva,
                null,
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        when(pagoRepository.existsByReservaId(1L))
                .thenReturn(false);

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals(
                "Monto inválido.",
                excepcion.getMessage()
        );

        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void crearDebeLanzarExcepcionCuandoMontoEsCero() {
        Reserva reserva = crearReserva(1L);

        Pago pago = crearPago(
                null,
                reserva,
                BigDecimal.ZERO,
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        when(pagoRepository.existsByReservaId(1L))
                .thenReturn(false);

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> pagoService.crear(pago)
        );

        assertEquals(
                "Monto inválido.",
                excepcion.getMessage()
        );

        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void listarDebeRetornarTodosLosPagos() {
        Pago pago1 = crearPago(
                1L,
                crearReserva(1L),
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        Pago pago2 = crearPago(
                2L,
                crearReserva(2L),
                new BigDecimal("250.00"),
                "TARJETA",
                EstadoPago.PAGADO
        );

        when(pagoRepository.findAll())
                .thenReturn(List.of(pago1, pago2));

        List<Pago> resultado = pagoService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(pago1, resultado.get(0));
        assertEquals(pago2, resultado.get(1));

        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void listarPagadosDebeRetornarSoloPagosPagados() {
        Pago pagoPagado = crearPago(
                1L,
                crearReserva(1L),
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PAGADO
        );

        when(pagoRepository.findByEstado(EstadoPago.PAGADO))
                .thenReturn(List.of(pagoPagado));

        List<Pago> resultado = pagoService.listarPagados();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(EstadoPago.PAGADO, resultado.get(0).getEstado());

        verify(pagoRepository, times(1))
                .findByEstado(EstadoPago.PAGADO);
    }

    @Test
    void buscarPorIdDebeRetornarPagoCuandoExiste() {
        Pago pago = crearPago(
                1L,
                crearReserva(1L),
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        Pago resultado = pagoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(new BigDecimal("150.00"), resultado.getMonto());
        assertEquals(EstadoPago.PENDIENTE, resultado.getEstado());

        verify(pagoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(pagoRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> pagoService.buscarPorId(99L)
        );

        assertEquals(
                "Pago no encontrado",
                excepcion.getMessage()
        );

        verify(pagoRepository, times(1)).findById(99L);
    }

    @Test
    void actualizarDebeModificarPagoPendiente() {
        Pago pagoExistente = crearPago(
                1L,
                crearReserva(1L),
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        Pago datosActualizados = crearPago(
                null,
                crearReserva(2L),
                new BigDecimal("200.00"),
                "TARJETA",
                EstadoPago.ANULADO
        );

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pagoExistente));

        when(pagoRepository.save(pagoExistente))
                .thenReturn(pagoExistente);

        Pago resultado = pagoService.actualizar(
                1L,
                datosActualizados
        );

        assertNotNull(resultado);
        assertEquals(new BigDecimal("200.00"), resultado.getMonto());
        assertEquals("TARJETA", resultado.getMetodoPago());
        assertEquals(EstadoPago.ANULADO, resultado.getEstado());

        // La reserva original no debe modificarse.
        assertEquals(1L, resultado.getReserva().getId());

        verify(pagoRepository, times(1)).findById(1L);
        verify(pagoRepository, times(1)).save(pagoExistente);
    }

    @Test
    void actualizarDebeLanzarExcepcionCuandoPagoEstaCompletado() {
        Pago pagoPagado = crearPago(
                1L,
                crearReserva(1L),
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PAGADO
        );

        Pago datosActualizados = crearPago(
                null,
                crearReserva(1L),
                new BigDecimal("200.00"),
                "TARJETA",
                EstadoPago.PENDIENTE
        );

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pagoPagado));

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> pagoService.actualizar(
                        1L,
                        datosActualizados
                )
        );

        assertEquals(
                "No se puede modificar un pago completado",
                excepcion.getMessage()
        );

        verify(pagoRepository, times(1)).findById(1L);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void eliminarDebeEliminarPagoPendiente() {
        Pago pagoPendiente = crearPago(
                1L,
                crearReserva(1L),
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PENDIENTE
        );

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pagoPendiente));

        pagoService.eliminar(1L);

        verify(pagoRepository, times(1)).findById(1L);
        verify(pagoRepository, times(1)).delete(pagoPendiente);
    }

    @Test
    void eliminarDebeLanzarExcepcionCuandoPagoEstaCompletado() {
        Pago pagoPagado = crearPago(
                1L,
                crearReserva(1L),
                new BigDecimal("150.00"),
                "EFECTIVO",
                EstadoPago.PAGADO
        );

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pagoPagado));

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> pagoService.eliminar(1L)
        );

        assertEquals(
                "No se puede eliminar un pago completado",
                excepcion.getMessage()
        );

        verify(pagoRepository, times(1)).findById(1L);
        verify(pagoRepository, never()).delete(any(Pago.class));
    }

    private Reserva crearReserva(Long id) {
        Reserva reserva = new Reserva();
        reserva.setId(id);
        return reserva;
    }

    private Pago crearPago(
            Long id,
            Reserva reserva,
            BigDecimal monto,
            String metodoPago,
            EstadoPago estado
    ) {
        Pago pago = new Pago();

        pago.setId(id);
        pago.setReserva(reserva);
        pago.setMonto(monto);
        pago.setMetodoPago(metodoPago);
        pago.setEstado(estado);
        pago.setFechaPago(LocalDate.of(2026, 7, 17));

        return pago;
    }
}