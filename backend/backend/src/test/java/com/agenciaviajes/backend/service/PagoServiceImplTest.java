package com.agenciaviajes.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
 
import java.math.BigDecimal;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import com.agenciaviajes.backend.enums.EstadoPago;
import com.agenciaviajes.backend.model.Pago;
import com.agenciaviajes.backend.model.Reserva;
import com.agenciaviajes.backend.repository.PagoRepository;
import com.agenciaviajes.backend.repository.ReservaRepository;
import com.agenciaviajes.backend.service.impl.PagoServiceImpl;
 
/**
 * Pruebas de caja blanca (cobertura de decision/condicion) para
 * PagoServiceImpl.
 * Juan Aguilera
 * Tecnica aplicada: cobertura de rama + cobertura de condicion sobre las
 * clausulas OR. Complejidad ciclomatica de crear() = 5 (4 decisiones + 1).
 * Complejidad de actualizar()/eliminar() = 2 (1 decision + 1) cada uno.
 *
 * Cada metodo de prueba esta etiquetado con el identificador de caso
 * (CP-xx) que corresponde a la tabla de diseno de casos del informe.
 */
@ExtendWith(MockitoExtension.class)
class PagoServiceImplTest {
 
    @Mock
    private PagoRepository pagoRepository;
 
    @Mock
    private ReservaRepository reservaRepository;
 
    private PagoServiceImpl pagoService;
 
    @BeforeEach
    void setUp() {
        pagoService = new PagoServiceImpl(pagoRepository, reservaRepository);
    }
 
    // ---------- Helpers ----------
 
    private Reserva reservaValida(Long id) {
        Reserva reserva = new Reserva();
        reserva.setId(id);
        return reserva;
    }
 
    private Pago pagoConReserva(Reserva reserva, BigDecimal monto, EstadoPago estado) {
        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMonto(monto);
        pago.setEstado(estado);
        return pago;
    }
 
    // ---------- crear(): CM = 5, 8 casos de prueba ----------
 
    @Nested
    class Crear {
 
        @Test
        void CP01_reservaNula_lanzaExcepcion() {
            Pago pago = pagoConReserva(null, BigDecimal.TEN, EstadoPago.PENDIENTE);
 
            assertThatThrownBy(() -> pagoService.crear(pago))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Debe seleccionar una reserva.");
        }
 
        @Test
        void CP02_reservaSinId_lanzaExcepcion() {
            Pago pago = pagoConReserva(new Reserva(), BigDecimal.TEN, EstadoPago.PENDIENTE);
 
            assertThatThrownBy(() -> pagoService.crear(pago))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Debe seleccionar una reserva.");
        }
 
        @Test
        void CP03_reservaNoExisteEnBD_lanzaExcepcion() {
            Reserva reserva = reservaValida(1L);
            Pago pago = pagoConReserva(reserva, BigDecimal.TEN, EstadoPago.PENDIENTE);
 
            when(reservaRepository.findById(1L)).thenReturn(java.util.Optional.empty());
 
            assertThatThrownBy(() -> pagoService.crear(pago))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("La reserva no existe.");
        }
 
        @Test
        void CP04_reservaYaTienePago_lanzaExcepcion() {
            Reserva reserva = reservaValida(1L);
            Pago pago = pagoConReserva(reserva, BigDecimal.TEN, EstadoPago.PENDIENTE);
 
            when(reservaRepository.findById(1L)).thenReturn(java.util.Optional.of(reserva));
            when(pagoRepository.existsByReservaId(1L)).thenReturn(true);
 
            assertThatThrownBy(() -> pagoService.crear(pago))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("La reserva ya tiene un pago registrado.");
        }
 
        @Test
        void CP05_montoNulo_lanzaExcepcion() {
            Reserva reserva = reservaValida(1L);
            Pago pago = pagoConReserva(reserva, null, EstadoPago.PENDIENTE);
 
            when(reservaRepository.findById(1L)).thenReturn(java.util.Optional.of(reserva));
            when(pagoRepository.existsByReservaId(1L)).thenReturn(false);
 
            assertThatThrownBy(() -> pagoService.crear(pago))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Monto inválido.");
        }
 
        @Test
        void CP06_montoMenorOIgualACero_lanzaExcepcion() {
            Reserva reserva = reservaValida(1L);
            Pago pago = pagoConReserva(reserva, BigDecimal.ZERO, EstadoPago.PENDIENTE);
 
            when(reservaRepository.findById(1L)).thenReturn(java.util.Optional.of(reserva));
            when(pagoRepository.existsByReservaId(1L)).thenReturn(false);
 
            assertThatThrownBy(() -> pagoService.crear(pago))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Monto inválido.");
        }
 
        @Test
        void CP07_estadoNulo_seAsignaPendientePorDefecto() {
            Reserva reserva = reservaValida(1L);
            Pago pago = pagoConReserva(reserva, BigDecimal.valueOf(150), null);
 
            when(reservaRepository.findById(1L)).thenReturn(java.util.Optional.of(reserva));
            when(pagoRepository.existsByReservaId(1L)).thenReturn(false);
            when(pagoRepository.save(pago)).thenReturn(pago);
 
            Pago resultado = pagoService.crear(pago);
 
            assertThat(resultado.getEstado()).isEqualTo(EstadoPago.PENDIENTE);
        }
 
        @Test
        void CP08_caminoFeliz_estadoEnviadoSeConservaYSeGuarda() {
            Reserva reserva = reservaValida(1L);
            Pago pago = pagoConReserva(reserva, BigDecimal.valueOf(200), EstadoPago.PAGADO);
 
            when(reservaRepository.findById(1L)).thenReturn(java.util.Optional.of(reserva));
            when(pagoRepository.existsByReservaId(1L)).thenReturn(false);
            when(pagoRepository.save(pago)).thenReturn(pago);
 
            Pago resultado = pagoService.crear(pago);
 
            assertThat(resultado.getEstado()).isEqualTo(EstadoPago.PAGADO);
            assertThat(resultado.getReserva()).isEqualTo(reserva);
        }
    }
 
    // ---------- actualizar(): CM = 2, 3 casos (incluye no-encontrado) ----------
 
    @Nested
    class Actualizar {
 
        @Test
        void CP09_pagoNoEncontrado_lanzaExcepcion() {
            when(pagoRepository.findById(99L)).thenReturn(java.util.Optional.empty());
 
            assertThatThrownBy(() -> pagoService.actualizar(99L, new Pago()))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Pago no encontrado");
        }
 
        @Test
        void CP10_pagoYaPagado_noSePuedeModificar() {
            Pago existente = new Pago();
            existente.setId(1L);
            existente.setEstado(EstadoPago.PAGADO);
 
            when(pagoRepository.findById(1L)).thenReturn(java.util.Optional.of(existente));
 
            assertThatThrownBy(() -> pagoService.actualizar(1L, new Pago()))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("No se puede modificar un pago completado");
        }
 
        @Test
        void CP11_pagoPendiente_seActualizaCorrectamente() {
            Pago existente = new Pago();
            existente.setId(1L);
            existente.setEstado(EstadoPago.PENDIENTE);
 
            Pago cambios = new Pago();
            cambios.setMonto(BigDecimal.valueOf(300));
            cambios.setMetodoPago("TARJETA");
            cambios.setEstado(EstadoPago.PAGADO);
 
            when(pagoRepository.findById(1L)).thenReturn(java.util.Optional.of(existente));
            when(pagoRepository.save(existente)).thenReturn(existente);
 
            Pago resultado = pagoService.actualizar(1L, cambios);
 
            assertThat(resultado.getMonto()).isEqualByComparingTo(BigDecimal.valueOf(300));
            assertThat(resultado.getEstado()).isEqualTo(EstadoPago.PAGADO);
        }
    }
 
    // ---------- eliminar(): CM = 2, 2 casos ----------
 
    @Nested
    class Eliminar {
 
        @Test
        void CP12_pagoPagado_noSePuedeEliminar() {
            Pago pago = new Pago();
            pago.setId(1L);
            pago.setEstado(EstadoPago.PAGADO);
 
            when(pagoRepository.findById(1L)).thenReturn(java.util.Optional.of(pago));
 
            assertThatThrownBy(() -> pagoService.eliminar(1L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("No se puede eliminar un pago completado");
        }
 
        @Test
        void CP13_pagoPendiente_seEliminaCorrectamente() {
            Pago pago = new Pago();
            pago.setId(1L);
            pago.setEstado(EstadoPago.PENDIENTE);
 
            when(pagoRepository.findById(1L)).thenReturn(java.util.Optional.of(pago));
 
            pagoService.eliminar(1L);
 
            org.mockito.Mockito.verify(pagoRepository).delete(pago);
        }
    }
}    
