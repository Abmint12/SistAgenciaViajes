package com.agenciaviajes.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agenciaviajes.backend.model.Factura;

public interface FacturaRepository extends JpaRepository<Factura,Integer>{
    
    
    boolean existsByNumeroFactura(String numeroFactura);

    boolean existsByPagoId(Long idPago);

}
