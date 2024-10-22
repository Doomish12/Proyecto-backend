package com.diego.login.dto.productosReponse;

import lombok.Data;

import java.io.Serializable;

@Data
public class ListaDeseoReponse implements Serializable {

    private Long id_Deseo;
    private Long id_Usuario;
    private Long id_Producto;
}
