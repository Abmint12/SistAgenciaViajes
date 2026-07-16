package com.agenciaviajes.backend.service;

import java.util.List;

import com.agenciaviajes.backend.model.Usuario;

public interface UsuarioService {
  
List<Usuario> listar();

Usuario buscarPorId (Integer id);

Usuario guardar (Usuario usuario);

void eliminar (Integer id);

Usuario actualizar(Integer id, Usuario usuario);

Usuario login(String nombreUsuario, String contrasena);
}
