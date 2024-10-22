package com.diego.login.dto.productos;

import com.diego.login.persistence.entity.Productos;
import com.diego.login.persistence.entity.Usuario;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveListaDeseos implements Serializable {

    @NotNull(message = "El usuario no puede ser nulo")
    private Long id_User;

    @NotNull(message = "El usuario no puede ser nulo")
    private Long id_Producto;
}
