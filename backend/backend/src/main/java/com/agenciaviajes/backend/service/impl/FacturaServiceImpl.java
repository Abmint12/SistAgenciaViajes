package com.agenciaviajes.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.agenciaviajes.backend.enums.EstadoPago;
import com.agenciaviajes.backend.model.Factura;
import com.agenciaviajes.backend.model.Pago;
import com.agenciaviajes.backend.repository.FacturaRepository;
import com.agenciaviajes.backend.repository.PagoRepository;
import com.agenciaviajes.backend.service.FacturaService;

@Service
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final PagoRepository pagoRepository;

    public FacturaServiceImpl(FacturaRepository facturaRepository,
            PagoRepository pagoRepository) {
        this.facturaRepository = facturaRepository;
        this.pagoRepository = pagoRepository;
    }

    @Override
    public List<Factura> listar() {
        return facturaRepository.findAll();
    }

    @Override
    public Optional<Factura> buscarPorId(Integer id) {
        return facturaRepository.findById(id);
    }

    @Override
    public Factura guardar(Factura factura) {

        if (factura.getPago() == null || factura.getPago().getId() == null) {
            throw new RuntimeException("Debe seleccionar un pago.");
        }

        Pago pago = pagoRepository.findById(factura.getPago().getId())
                .orElseThrow(() -> new RuntimeException("El pago no existe."));
        if (pago.getEstado() != EstadoPago.PAGADO) {
            throw new RuntimeException(
                    "Solo se pueden emitir facturas para pagos PAGADOS.");
        }

        if (facturaRepository.existsByNumeroFactura(factura.getNumeroFactura())) {
            throw new RuntimeException("El número de factura ya existe.");
        }

        if (facturaRepository.existsByPagoId(pago.getId())) {
            throw new RuntimeException("Este pago ya tiene una factura registrada.");
        }

        if (factura.getSubtotal() == null ||
                factura.getImpuesto() == null ||
                factura.getTotal() == null) {
            throw new RuntimeException("Los montos son obligatorios.");
        }

        if (factura.getFechaEmision() == null) {
            factura.setFechaEmision(LocalDateTime.now());
        }

        factura.setPago(pago);
        factura.setEstado("EMITIDA");
        return facturaRepository.save(factura);
    }

    @Override
    public Factura actualizar(Integer id, Factura factura) {

        Factura facturaExistente = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada."));

        if (factura.getPago() == null || factura.getPago().getId() == null) {
            throw new RuntimeException("Debe seleccionar un pago.");
        }

        Pago pago = pagoRepository.findById(factura.getPago().getId())
                .orElseThrow(() -> new RuntimeException("El pago no existe."));

        if (!facturaExistente.getNumeroFactura().equals(factura.getNumeroFactura())
                && facturaRepository.existsByNumeroFactura(factura.getNumeroFactura())) {
            throw new RuntimeException("El número de factura ya existe.");
        }

        facturaExistente.setPago(pago);
        facturaExistente.setNumeroFactura(factura.getNumeroFactura());
        facturaExistente.setFechaEmision(factura.getFechaEmision());
        facturaExistente.setSubtotal(factura.getSubtotal());
        facturaExistente.setImpuesto(factura.getImpuesto());
        facturaExistente.setTotal(factura.getTotal());

        return facturaRepository.save(facturaExistente);
    }

    @Override
    public void eliminar(Integer id) {

        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada."));

        factura.setEstado("ANULADA");

        facturaRepository.save(factura);
    }

}
