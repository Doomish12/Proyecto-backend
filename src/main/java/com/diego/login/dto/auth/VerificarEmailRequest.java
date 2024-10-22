package com.diego.login.dto.auth;

import lombok.Data;

import java.io.Serializable;

@Data
public class VerificarEmailRequest implements Serializable {
    private String email;
    private String codigo;
}
