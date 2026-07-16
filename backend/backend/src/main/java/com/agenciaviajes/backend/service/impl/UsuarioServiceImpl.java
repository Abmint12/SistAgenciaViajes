package com.agenciaviajes.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.agenciaviajes.backend.exception.ResourceNotFoundException;
import com.agenciaviajes.backend.model.Usuario;
import com.agenciaviajes.backend.repository.UsuarioRepository;
import com.agenciaviajes.backend.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public List<Usuario> listar() {
       
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarPorId(Integer id) {
       
        return usuarioRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado con ID: "+id));
    }

    @Override
    public Usuario guardar(Usuario usuario) {
       return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Integer id) {
        Usuario usuario=buscarPorId(id);
        usuarioRepository.delete(usuario);
    }


    @Override
    public Usuario actualizar(Integer id, Usuario usuario) {
            Usuario usuarioExistente=  buscarPorId(id);

            usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
            usuarioExistente.setContrasena(usuario.getContrasena());
            usuarioExistente.setRol(usuario.getRol());

            return usuarioRepository.save(usuarioExistente);
    }

    @Override
public Usuario login(String nombreUsuario, String contrasena) {

    Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    if (!usuario.getContrasena().equals(contrasena)) {
        throw new RuntimeException("Contraseña incorrecta");
    }

    if (!usuario.getActivo()) {
        throw new RuntimeException("Usuario inactivo");
    }

    return usuario;
}
}
