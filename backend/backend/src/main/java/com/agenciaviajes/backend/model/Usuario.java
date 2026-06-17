package com.agenciaviajes.backend.model;

import com.agenciaviajes.backend.enums.RolUsuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message= "El nombre de usuario es obligatorio")
    @Size(max = 50)
    @Column (name ="nombre_usuario", nullable = false, unique =true, length = 50)
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es obligaotria")
    @Size(max= 255)
    @Column(nullable = false)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length=20)
    private RolUsuario rol=RolUsuario.VENDEDOR;

    @Column(nullable = false)
    private Boolean activo=true;

}
