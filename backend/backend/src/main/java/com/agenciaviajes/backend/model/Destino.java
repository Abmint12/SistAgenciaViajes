package com.agenciaviajes.backend.model;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="destinos")
@Getter
@Setter
@NoArgsConstructor
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer idDestino;

    @NotBlank(message = "EL nombre del destino es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message ="El pais es obligatorio")
    @Column(nullable = false)
    private String pais;

    @NotBlank(message ="La ciudad es obligatoria")
    @Column(nullable = false)
    private String ciudad;
   

    @Column(columnDefinition ="TEXT")
    private String descripcion;
}
