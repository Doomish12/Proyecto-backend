package com.diego.login.dto.productosReponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class CarritoProdResponse implements Serializable {

    private Long id_Carrito;
    private Long id_Usuario;
    private Long id_Producto;
    private int cantidad;
    private Double totalCarrito;

    private String mensaje;


}
