package com.diego.login.dto.auth;

import com.diego.login.persistence.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class VerificarEmailResponse implements Serializable {

    private String mensaje;
    private boolean exito;
    private Usuario usuario;
    private String token;

}
