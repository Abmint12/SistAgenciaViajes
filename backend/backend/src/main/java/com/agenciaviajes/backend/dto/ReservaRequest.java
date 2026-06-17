package com.agenciaviajes.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ReservaRequest {

    private Long idCliente;
    private Integer idDestino;
    private LocalDate fechaViaje;
    private Integer cantPasajes;
    private BigDecimal costoTotal;
    private String estado;

}