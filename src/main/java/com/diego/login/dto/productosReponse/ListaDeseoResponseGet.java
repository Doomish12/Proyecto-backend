package com.diego.login.dto.productosReponse;

import com.diego.login.persistence.entity.Productos;
import lombok.Data;

import java.io.Serializable;

@Data
public class ListaDeseoResponseGet implements Serializable {

    private Long id_Deseo;
    private Productos productos;

    public ListaDeseoResponseGet(Long id_Deseo, Productos productos) {
        this.id_Deseo = id_Deseo;
        this.productos = productos;
    }

}
