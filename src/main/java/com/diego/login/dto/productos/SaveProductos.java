package com.diego.login.dto.productos;

import com.diego.login.persistence.entity.CategoriaProd;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaveProductos implements Serializable {

    @NotNull(message = "El precio no puede ser nulo")
    @Min(value = 10, message = "El precio debe ser al menos 10")
    private Double precio_pro;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nombre_pro;

    @NotBlank(message = "La descripción del producto no puede estar vacía")
    private String descripcion_pro;

   // @NotBlank(message = "La imagen del producto no puede estar vacía")
   // private String imagen_pro;

    @NotNull(message = "Las imágenes no pueden estar vacías")
    @Size(min = 1, message = "Debes proporcionar al menos una imagen") // Asegura al menos una imagen
    private List<@NotBlank(message = "La imagen no puede estar vacía") @Size(max = 300, message = "La URL de la imagen no puede exceder los 300 caracteres") String> imagen_pro;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock_pro;

    @NotNull(message = "El estado no puede ser nulo")
    private String estado_pro;

    @NotNull(message = "El stock no puede ser nulo")
    private Long id_categoria_prod;
}
