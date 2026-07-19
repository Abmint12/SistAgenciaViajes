package com.agenciaviajes.backend.service.impl;

import com.agenciaviajes.backend.enums.EstadoPago;
import com.agenciaviajes.backend.model.Factura;
import com.agenciaviajes.backend.model.Pago;
import com.agenciaviajes.backend.repository.FacturaRepository;
import com.agenciaviajes.backend.repository.PagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturaServiceImplTest {

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private FacturaServiceImpl facturaService;

    @Test
    void listarDebeRetornarTodasLasFacturas() {
        Factura factura1 = crearFactura(
                1,
                crearPago(1L, EstadoPago.PAGADO),
                "FAC-001"
        );

        Factura factura2 = crearFactura(
                2,
                crearPago(2L, EstadoPago.PAGADO),
                "FAC-002"
        );

        when(facturaRepository.findAll())
                .thenReturn(List.of(factura1, factura2));

        List<Factura> resultado = facturaService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("FAC-001", resultado.get(0).getNumeroFactura());
        assertEquals("FAC-002", resultado.get(1).getNumeroFactura());

        verify(facturaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarFacturaCuandoExiste() {
        Factura factura = crearFactura(
                1,
                crearPago(1L, EstadoPago.PAGADO),
                "FAC-001"
        );

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(factura));

        Optional<Factura> resultado = facturaService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        assertEquals("FAC-001", resultado.get().getNumeroFactura());

        verify(facturaRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorIdDebeRetornarVacioCuandoNoExiste() {
        when(facturaRepository.findById(99))
                .thenReturn(Optional.empty());

        Optional<Factura> resultado = facturaService.buscarPorId(99);

        assertTrue(resultado.isEmpty());

        verify(facturaRepository, times(1)).findById(99);
    }

    @Test
    void guardarDebeGuardarFacturaValida() {
        Pago pago = crearPago(1L, EstadoPago.PAGADO);
        Factura factura = crearFactura(null, pago, "FAC-001");

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        when(facturaRepository.existsByNumeroFactura("FAC-001"))
                .thenReturn(false);

        when(facturaRepository.existsByPagoId(1L))
                .thenReturn(false);

        when(facturaRepository.save(factura))
                .thenReturn(factura);

        Factura resultado = facturaService.guardar(factura);

        assertNotNull(resultado);
        assertEquals("FAC-001", resultado.getNumeroFactura());
        assertEquals("EMITIDA", resultado.getEstado());
        assertEquals(pago, resultado.getPago());
        assertNotNull(resultado.getFechaEmision());

        verify(pagoRepository, times(1)).findById(1L);
        verify(facturaRepository, times(1))
                .existsByNumeroFactura("FAC-001");
        verify(facturaRepository, times(1)).existsByPagoId(1L);
        verify(facturaRepository, times(1)).save(factura);
    }

    @Test
    void guardarDebeLanzarExcepcionCuandoNoSeSeleccionaPago() {
        Factura factura = crearFactura(null, null, "FAC-001");

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.guardar(factura)
        );

        assertEquals(
                "Debe seleccionar un pago.",
                excepcion.getMessage()
        );

        verifyNoInteractions(pagoRepository);
        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void guardarDebeLanzarExcepcionCuandoPagoNoTieneId() {
        Pago pagoSinId = crearPago(null, EstadoPago.PAGADO);
        Factura factura = crearFactura(null, pagoSinId, "FAC-001");

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.guardar(factura)
        );

        assertEquals(
                "Debe seleccionar un pago.",
                excepcion.getMessage()
        );

        verifyNoInteractions(pagoRepository);
        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void guardarDebeLanzarExcepcionCuandoPagoNoExiste() {
        Pago pago = crearPago(99L, EstadoPago.PAGADO);
        Factura factura = crearFactura(null, pago, "FAC-001");

        when(pagoRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.guardar(factura)
        );

        assertEquals(
                "El pago no existe.",
                excepcion.getMessage()
        );

        verify(pagoRepository, times(1)).findById(99L);
        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void guardarDebeLanzarExcepcionCuandoPagoNoEstaPagado() {
        Pago pago = crearPago(1L, EstadoPago.PENDIENTE);
        Factura factura = crearFactura(null, pago, "FAC-001");

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.guardar(factura)
        );

        assertEquals(
                "Solo se pueden emitir facturas para pagos PAGADOS.",
                excepcion.getMessage()
        );

        verify(pagoRepository, times(1)).findById(1L);
        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void guardarDebeLanzarExcepcionCuandoNumeroFacturaYaExiste() {
        Pago pago = crearPago(1L, EstadoPago.PAGADO);
        Factura factura = crearFactura(null, pago, "FAC-001");

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        when(facturaRepository.existsByNumeroFactura("FAC-001"))
                .thenReturn(true);

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.guardar(factura)
        );

        assertEquals(
                "El número de factura ya existe.",
                excepcion.getMessage()
        );

        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void guardarDebeLanzarExcepcionCuandoPagoYaTieneFactura() {
        Pago pago = crearPago(1L, EstadoPago.PAGADO);
        Factura factura = crearFactura(null, pago, "FAC-001");

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        when(facturaRepository.existsByNumeroFactura("FAC-001"))
                .thenReturn(false);

        when(facturaRepository.existsByPagoId(1L))
                .thenReturn(true);

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.guardar(factura)
        );

        assertEquals(
                "Este pago ya tiene una factura registrada.",
                excepcion.getMessage()
        );

        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void guardarDebeLanzarExcepcionCuandoMontosSonNulos() {
        Pago pago = crearPago(1L, EstadoPago.PAGADO);
        Factura factura = crearFactura(null, pago, "FAC-001");

        factura.setSubtotal(null);
        factura.setImpuesto(null);
        factura.setTotal(null);

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        when(facturaRepository.existsByNumeroFactura("FAC-001"))
                .thenReturn(false);

        when(facturaRepository.existsByPagoId(1L))
                .thenReturn(false);

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.guardar(factura)
        );

        assertEquals(
                "Los montos son obligatorios.",
                excepcion.getMessage()
        );

        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void actualizarDebeActualizarFacturaValida() {
        Pago pago = crearPago(1L, EstadoPago.PAGADO);

        Factura facturaExistente = crearFactura(
                1,
                pago,
                "FAC-001"
        );

        Factura datosActualizados = crearFactura(
                null,
                pago,
                "FAC-002"
        );

        datosActualizados.setSubtotal(new BigDecimal("200.00"));
        datosActualizados.setImpuesto(new BigDecimal("30.00"));
        datosActualizados.setTotal(new BigDecimal("230.00"));

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(facturaExistente));

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        when(facturaRepository.existsByNumeroFactura("FAC-002"))
                .thenReturn(false);

        when(facturaRepository.save(facturaExistente))
                .thenReturn(facturaExistente);

        Factura resultado = facturaService.actualizar(
                1,
                datosActualizados
        );

        assertNotNull(resultado);
        assertEquals("FAC-002", resultado.getNumeroFactura());
        assertEquals(
                new BigDecimal("200.00"),
                resultado.getSubtotal()
        );
        assertEquals(
                new BigDecimal("30.00"),
                resultado.getImpuesto()
        );
        assertEquals(
                new BigDecimal("230.00"),
                resultado.getTotal()
        );
        assertEquals(pago, resultado.getPago());

        verify(facturaRepository, times(1)).findById(1);
        verify(pagoRepository, times(1)).findById(1L);
        verify(facturaRepository, times(1))
                .save(facturaExistente);
    }

    @Test
    void actualizarDebeLanzarExcepcionCuandoFacturaNoExiste() {
        Pago pago = crearPago(1L, EstadoPago.PAGADO);
        Factura datosActualizados = crearFactura(
                null,
                pago,
                "FAC-002"
        );

        when(facturaRepository.findById(99))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.actualizar(
                        99,
                        datosActualizados
                )
        );

        assertEquals(
                "Factura no encontrada.",
                excepcion.getMessage()
        );

        verify(facturaRepository, times(1)).findById(99);
        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionCuandoNoSeleccionaPago() {
        Factura facturaExistente = crearFactura(
                1,
                crearPago(1L, EstadoPago.PAGADO),
                "FAC-001"
        );

        Factura datosActualizados = crearFactura(
                null,
                null,
                "FAC-002"
        );

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(facturaExistente));

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.actualizar(
                        1,
                        datosActualizados
                )
        );

        assertEquals(
                "Debe seleccionar un pago.",
                excepcion.getMessage()
        );

        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionCuandoPagoNoEstaPagado() {
        Pago pagoPendiente = crearPago(
                1L,
                EstadoPago.PENDIENTE
        );

        Factura facturaExistente = crearFactura(
                1,
                pagoPendiente,
                "FAC-001"
        );

        Factura datosActualizados = crearFactura(
                null,
                pagoPendiente,
                "FAC-002"
        );

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(facturaExistente));

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pagoPendiente));

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.actualizar(
                        1,
                        datosActualizados
                )
        );

        assertEquals(
                "Solo se puede emitir factura para un pago PAGADO.",
                excepcion.getMessage()
        );

        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void eliminarDebeCambiarEstadoAAnulada() {
        Factura factura = crearFactura(
                1,
                crearPago(1L, EstadoPago.PAGADO),
                "FAC-001"
        );

        when(facturaRepository.findById(1))
                .thenReturn(Optional.of(factura));

        when(facturaRepository.save(factura))
                .thenReturn(factura);

        facturaService.eliminar(1);

        assertEquals("ANULADA", factura.getEstado());

        verify(facturaRepository, times(1)).findById(1);
        verify(facturaRepository, times(1)).save(factura);
    }

    @Test
    void eliminarDebeLanzarExcepcionCuandoFacturaNoExiste() {
        when(facturaRepository.findById(99))
                .thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> facturaService.eliminar(99)
        );

        assertEquals(
                "Factura no encontrada.",
                excepcion.getMessage()
        );

        verify(facturaRepository, times(1)).findById(99);
        verify(facturaRepository, never()).save(any(Factura.class));
    }

    private Factura crearFactura(
            Integer id,
            Pago pago,
            String numeroFactura
    ) {
        Factura factura = new Factura();

        factura.setId(id);
        factura.setPago(pago);
        factura.setNumeroFactura(numeroFactura);
        factura.setFechaEmision(null);
        factura.setEstado("EMITIDA");
        factura.setSubtotal(new BigDecimal("100.00"));
        factura.setImpuesto(new BigDecimal("15.00"));
        factura.setTotal(new BigDecimal("115.00"));

        return factura;
    }

    private Pago crearPago(
            Long id,
            EstadoPago estado
    ) {
        Pago pago = new Pago();

        pago.setId(id);
        pago.setEstado(estado);

        return pago;
    }
}