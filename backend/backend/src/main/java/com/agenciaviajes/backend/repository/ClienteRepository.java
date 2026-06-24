package com.agenciaviajes.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agenciaviajes.backend.model.*;

public interface ClienteRepository
 extends JpaRepository<Cliente,Long>{
}
