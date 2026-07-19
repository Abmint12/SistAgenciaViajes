package com.agenciaviajes.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
 
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import com.agenciaviajes.backend.dto.ReservaResponse;
import com.agenciaviajes.backend.model.Cliente;
import com.agenciaviajes.backend.model.Destino;
import com.agenciaviajes.backend.model.Reserva;
import com.agenciaviajes.backend.repository.ClienteRepository;
import com.agenciaviajes.backend.repository.DestinoRepository;
import com.agenciaviajes.backend.repository.ReservaRepository;
import com.agenciaviajes.backend.service.impl.ReservaServiceImpl;
 
/**
 * Pruebas de caja blanca - tecnica de cobertura de bucles (loop coverage).
 *
 * ReservaServiceImpl.listar() es el unico metodo del backend con una
 * estructura de iteracion explicita:
 *Juan Aguilera
 *   reservaRepository.findAll().stream()
 *       .map(this::convertirResponse)
 *       .collect(Collectors.toList());
 *
 * Se aplica el criterio clasico de Beizer para bucles simples: 0
 * iteraciones (bypass), 1 iteracion (paso simple) y N iteraciones
 * (paso multiple), verificando ademas que el orden y el contenido de
 * cada elemento mapeado sea correcto.
 */
@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {
 
    @Mock
    private ReservaRepository reservaRepository;
 
    @Mock
    private ClienteRepository clienteRepository;
 
    @Mock
    private DestinoRepository destinoRepository;
 
    private ReservaServiceImpl reservaService;
 
    @BeforeEach
    void setUp() {
        reservaService = new ReservaServiceImpl(reservaRepository, clienteRepository, destinoRepository);
    }
 
    private Reserva construirReserva(Long id, String nombreCliente, String nombreDestino) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre(nombreCliente);
        cliente.setApellido("Perez");
 
        Destino destino = new Destino();
        destino.setIdDestino(id.intValue());
        destino.setNombre(nombreDestino);
        destino.setPais("Ecuador");
        destino.setCiudad("Guayaquil");
 
        Reserva reserva = new Reserva();
        reserva.setId(id);
        reserva.setCliente(cliente);
        reserva.setDestino(destino);
        reserva.setFechaViaje(LocalDate.of(2026, 8, 1));
        reserva.setCantPasajes(2);
        reserva.setCostoTotal(BigDecimal.valueOf(500));
        reserva.setEstado("CONFIRMADA");
 
        return reserva;
    }
 
    // TC-L1: 0 iteraciones -> el bucle no se ejecuta ninguna vez
    @Test
    void TCL1_listaVacia_elBucleNoSeEjecuta() {
        when(reservaRepository.findAll()).thenReturn(Collections.emptyList());
 
        List<ReservaResponse> resultado = reservaService.listar();
 
        assertThat(resultado).isEmpty();
    }
 
    // TC-L2: 1 iteracion -> el bucle se ejecuta una sola vez
    @Test
    void TCL2_unaReserva_elBucleSeEjecutaUnaVez() {
        Reserva reserva = construirReserva(1L, "Ana", "Cancun");
 
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));
 
        List<ReservaResponse> resultado = reservaService.listar();
 
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreCliente()).isEqualTo("Ana Perez");
        assertThat(resultado.get(0).getNombreDestino()).isEqualTo("Cancun");
    }
 
    // TC-L3: N iteraciones (multiples) -> el bucle se ejecuta varias veces
    // y conserva el orden y el mapeo correcto de cada elemento
    @Test
    void TCL3_multiplesReservas_elBucleSeEjecutaNVeces() {
        Reserva r1 = construirReserva(1L, "Ana", "Cancun");
        Reserva r2 = construirReserva(2L, "Luis", "Paris");
        Reserva r3 = construirReserva(3L, "Marta", "Roma");
 
        when(reservaRepository.findAll()).thenReturn(List.of(r1, r2, r3));
 
        List<ReservaResponse> resultado = reservaService.listar();
 
        assertThat(resultado).hasSize(3);
        assertThat(resultado)
                .extracting(ReservaResponse::getNombreDestino)
                .containsExactly("Cancun", "Paris", "Roma");
    }
}