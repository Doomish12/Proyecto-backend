package com.diego.login.dto.productos;

import com.diego.login.persistence.entity.Productos;
import com.diego.login.persistence.entity.Usuario;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveCarritoProd implements Serializable {


    @NotNull(message = "El ID de usuario no puede ser nulo")
    private Long id_Usuario;

    @NotNull(message = "El ID de producto no puede ser nulo")
    private Long id_Producto;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    @NotNull(message = "No puede ser nulo el total carrito")
    @Positive(message = "El total del carrito debe ser positivo")
    private Double totalCarrito;

    private String mensaje;

}
