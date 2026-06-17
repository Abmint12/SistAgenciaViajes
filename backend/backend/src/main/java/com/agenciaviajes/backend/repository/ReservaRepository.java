package com.agenciaviajes.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agenciaviajes.backend.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long>{

}