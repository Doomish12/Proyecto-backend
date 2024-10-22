package com.diego.login.dto;

import com.diego.login.persistence.util.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String username;
    private String email;
    private Rol rol;
    private String imagen_user;
}
