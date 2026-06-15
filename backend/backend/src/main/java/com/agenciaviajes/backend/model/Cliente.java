package com.agenciaviajes.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name="clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nombre;

    private String apellido;

    @Column(unique=true)
    private String cedula;

    private String telefono;

    private String correo;
    
    private String direccion;

}
