package com.agenciaviajes.backend.repository;


import com.agenciaviajes.backend.enums.EstadoPago;
import com.agenciaviajes.backend.model.Pago;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    boolean existsByReservaId(Long reservaId);
    List<Pago> findByEstado(EstadoPago estado);
}