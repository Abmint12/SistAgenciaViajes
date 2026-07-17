package com.agenciaviajes.backend.service.impl;

import com.agenciaviajes.backend.enums.RolUsuario;
import com.agenciaviajes.backend.exception.ResourceNotFoundException;
import com.agenciaviajes.backend.model.Usuario;
import com.agenciaviajes.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void listarDebeRetornarTodosLosUsuarios() {
        Usuario usuario1 = crearUsuario(
                1,
                "admin",
                "admin123",
                RolUsuario.ADMIN,
                true
        );

        Usuario usuario2 = crearUsuario(
                2,
                "vendedor",
                "venta123",
                RolUsuario.VENDEDOR,
                true
        );

        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario1, usuario2));

        List<Usuario> resultado = usuarioService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("admin", resultado.get(0).getNombreUsuario());
        assertEquals("vendedor", resultado.get(1).getNombreUsuario());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarUsuarioCuandoExiste() {
        Usuario usuario = crearUsuario(
                1,
                "admin",
                "admin123",
                RolUsuario.ADMIN,
                true
        );

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("admin", resultado.getNombreUsuario());
        assertEquals(RolUsuario.ADMIN, resultado.getRol());
        assertTrue(resultado.getActivo());

        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(usuarioRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.buscarPorId(99)
        );

        verify(usuarioRepository, times(1)).findById(99);
    }

    @Test
    void guardarDebeGuardarUsuarioCorrectamente() {
        Usuario usuario = crearUsuario(
                null,
                "nuevoUsuario",
                "clave123",
                RolUsuario.VENDEDOR,
                true
        );

        Usuario usuarioGuardado = crearUsuario(
                1,
                "nuevoUsuario",
                "clave123",
                RolUsuario.VENDEDOR,
                true
        );

        when(usuarioRepository.save(usuario))
                .thenReturn(usuarioGuardado);

        Usuario resultado = usuarioService.guardar(usuario);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("nuevoUsuario", resultado.getNombreUsuario());
        assertEquals("clave123", resultado.getContrasena());
        assertEquals(RolUsuario.VENDEDOR, resultado.getRol());
        assertTrue(resultado.getActivo());

        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void actualizarDebeModificarUsuarioCuandoExiste() {
        Usuario usuarioExistente = crearUsuario(
                1,
                "usuarioAnterior",
                "claveAnterior",
                RolUsuario.VENDEDOR,
                true
        );

        Usuario nuevosDatos = crearUsuario(
                null,
                "usuarioActualizado",
                "claveNueva",
                RolUsuario.ADMIN,
                true
        );

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.save(usuarioExistente))
                .thenReturn(usuarioExistente);

        Usuario resultado = usuarioService.actualizar(
                1,
                nuevosDatos
        );

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(
                "usuarioActualizado",
                resultado.getNombreUsuario()
        );
        assertEquals(
                "claveNueva",
                resultado.getContrasena()
        );
        assertEquals(
                RolUsuario.ADMIN,
                resultado.getRol()
        );

        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, times(1))
                .save(usuarioExistente);
    }

    @Test
    void actualizarDebeLanzarExcepcionCuandoUsuarioNoExiste() {
        Usuario nuevosDatos = crearUsuario(
                null,
                "usuarioActualizado",
                "claveNueva",
                RolUsuario.ADMIN,
                true
        );

        when(usuarioRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.actualizar(
                        99,
                        nuevosDatos
                )
        );

        verify(usuarioRepository, times(1)).findById(99);
        verify(usuarioRepository, never())
                .save(any(Usuario.class));
    }

    @Test
    void eliminarDebeEliminarUsuarioCuandoExiste() {
        Usuario usuario = crearUsuario(
                1,
                "usuario",
                "clave123",
                RolUsuario.VENDEDOR,
                true
        );

        when(usuarioRepository.findById(1))
                .thenReturn(Optional.of(usuario));

        usuarioService.eliminar(1);

        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void eliminarDebeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.eliminar(99)
        );

        verify(usuarioRepository, times(1)).findById(99);
        verify(usuarioRepository, never())
                .delete(any(Usuario.class));
    }

    @Test
    void loginDebeRetornarUsuarioCuandoCredencialesSonCorrectas() {
        Usuario usuario = crearUsuario(
                1,
                "admin",
                "admin123",
                RolUsuario.ADMIN,
                true
        );

        when(usuarioRepository.findByNombreUsuario("admin"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.login(
                "admin",
                "admin123"
        );

        assertNotNull(resultado);
        assertEquals("admin", resultado.getNombreUsuario());
        assertEquals("admin123", resultado.getContrasena());
        assertEquals(RolUsuario.ADMIN, resultado.getRol());
        assertTrue(resultado.getActivo());

        verify(usuarioRepository, times(1))
                .findByNombreUsuario("admin");
    }

    @Test
    void loginDebeLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findByNombreUsuario("desconocido"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.login(
                        "desconocido",
                        "clave123"
                )
        );

        assertEquals(
                "Usuario no encontrado",
                excepcion.getMessage()
        );

        verify(usuarioRepository, times(1))
                .findByNombreUsuario("desconocido");
    }

    @Test
    void loginDebeLanzarExcepcionCuandoContrasenaEsIncorrecta() {
        Usuario usuario = crearUsuario(
                1,
                "admin",
                "admin123",
                RolUsuario.ADMIN,
                true
        );

        when(usuarioRepository.findByNombreUsuario("admin"))
                .thenReturn(Optional.of(usuario));

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> usuarioService.login(
                        "admin",
                        "claveIncorrecta"
                )
        );

        assertEquals(
                "Contraseña incorrecta",
                excepcion.getMessage()
        );

        verify(usuarioRepository, times(1))
                .findByNombreUsuario("admin");
    }

    @Test
    void loginDebeLanzarExcepcionCuandoUsuarioEstaInactivo() {
        Usuario usuarioInactivo = crearUsuario(
                1,
                "usuarioInactivo",
                "clave123",
                RolUsuario.VENDEDOR,
                false
        );

        when(usuarioRepository.findByNombreUsuario("usuarioInactivo"))
                .thenReturn(Optional.of(usuarioInactivo));

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> usuarioService.login(
                        "usuarioInactivo",
                        "clave123"
                )
        );

        assertEquals(
                "Usuario inactivo",
                excepcion.getMessage()
        );

        verify(usuarioRepository, times(1))
                .findByNombreUsuario("usuarioInactivo");
    }

    private Usuario crearUsuario(
            Integer id,
            String nombreUsuario,
            String contrasena,
            RolUsuario rol,
            Boolean activo
    ) {
        Usuario usuario = new Usuario();

        usuario.setId(id);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setContrasena(contrasena);
        usuario.setRol(rol);
        usuario.setActivo(activo);

        return usuario;
    }
}
