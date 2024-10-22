package com.diego.login.dto.productosReponse;

import com.diego.login.persistence.entity.Productos;
import lombok.Data;

import java.io.Serializable;

@Data
public class CarritoProdResponseGet implements Serializable {
    private Long idCarrito;
    private Productos productos;
    private Integer cantidad;
    private Double totalCarrito;

    // Constructor, getters y setters

    public CarritoProdResponseGet(Long idCarrito, Productos productos, Integer cantidad, Double totalCarrito) {
        this.idCarrito = idCarrito;
        this.productos = productos;
        this.cantidad = cantidad;
        this.totalCarrito = totalCarrito;
    }
}
