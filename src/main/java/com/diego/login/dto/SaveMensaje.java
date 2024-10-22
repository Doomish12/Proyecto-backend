package com.diego.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveMensaje  implements Serializable {

    @NotBlank
    @Size(min = 3, message = "El nombre debe tener al menos 4 caracteres")
    private String nombre;

    @NotBlank
    @Email(message = "El correo electrónico debe ser válido")
    private String email;

    @NotBlank(message = "El mensaje no puede estar vacio")
    private String mensaje;
}
