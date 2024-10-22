package com.diego.login.persistence.repository;

import com.diego.login.persistence.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su nombre de usuario
    Optional<Usuario> findByUsername(String username);

    // Busca un usuario por su correo electrónico
    Optional<Usuario> findByEmail(String email);

    // Verifica si existe un usuario con el nombre de usuario especificado
    boolean existsByUsername(String username);

    // Verifica si existe un usuario con el correo electrónico especificado
    boolean existsByEmail(String email);

    // Encuentra usuarios que no han verificado su correo electrónico y cuyo código de verificación ha expirado
    List<Usuario> findByEmailVerificacionFalseAndFechaCodigoVerficacionBefore(LocalDateTime expirationTime);
}
