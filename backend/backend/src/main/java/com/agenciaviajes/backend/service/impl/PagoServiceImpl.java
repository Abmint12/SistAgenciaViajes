package com.agenciaviajes.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.agenciaviajes.backend.enums.EstadoPago;
import com.agenciaviajes.backend.model.Pago;
import com.agenciaviajes.backend.model.Reserva;
import com.agenciaviajes.backend.repository.PagoRepository;
import com.agenciaviajes.backend.repository.ReservaRepository;
import com.agenciaviajes.backend.service.PagoService;

@Service
public class PagoServiceImpl implements PagoService{
    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;

    public PagoServiceImpl(PagoRepository pagoRepository,
                           ReservaRepository reservaRepository) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public Pago crear(Pago pago) {

        Reserva reserva = reservaRepository.findById(
                pago.getReserva().getId()
        ).orElseThrow(() -> new RuntimeException("Reserva no existe"));

        if (pago.getMonto() == null || pago.getMonto().doubleValue() <= 0) {
            throw new RuntimeException("Monto inválido");
        }

        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setReserva(reserva);

        return pagoRepository.save(pago);
    }

    @Override
    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    @Override
    public Pago buscarPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    @Override
    public Pago actualizar(Long id, Pago pago) {

        Pago existente = buscarPorId(id);

        // ❗ regla V&V: no cambiar reserva si ya existe pago confirmado
        if (existente.getEstado() == EstadoPago.PAGADO) {
            throw new RuntimeException("No se puede modificar un pago completado");
        }

        existente.setMonto(pago.getMonto());
        existente.setMetodoPago(pago.getMetodoPago());
        existente.setEstado(pago.getEstado());

        return pagoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Pago pago = buscarPorId(id);

        if (pago.getEstado() == EstadoPago.PAGADO) {
            throw new RuntimeException("No se puede eliminar un pago completado");
        }

        pagoRepository.delete(pago);
    }
}