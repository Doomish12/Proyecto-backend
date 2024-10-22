package com.diego.login.persistence.entity;

import com.diego.login.persistence.util.EstadoUsuario;
import com.diego.login.persistence.util.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(length = 300)
    private String imagen_user;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private EstadoUsuario estado;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column(nullable = false)
    private String verificacionCodigo; // Código de verificación

    @Column(nullable = true)
    private LocalDateTime fechaCodigoVerficacion; // Fecha de creación del código de verificación

    @Column(name = "email_Verificacion")
    private boolean emailVerificacion; // Estado de verificación del correo


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (rol == null) {
            return Collections.emptyList();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
