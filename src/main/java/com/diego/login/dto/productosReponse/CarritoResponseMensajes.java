package com.diego.login.dto.productosReponse;

import com.diego.login.persistence.entity.CarritoProd;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarritoResponseMensajes implements Serializable {

    private CarritoProdResponse carritoProdResponse;
    private boolean actualizado;



}
