package com.diego.login.persistence.interfaces;

import com.diego.login.dto.productos.SaveCarritoProd;
import com.diego.login.dto.productosReponse.CarritoProdResponse;
import com.diego.login.dto.productosReponse.CarritoProdResponseGet;
import com.diego.login.persistence.entity.CarritoProd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICarritoProd {

    Page<CarritoProdResponseGet> getCarritoProds(Long id, Pageable pageable);

    CarritoProdResponse guardar(SaveCarritoProd carritoProd);

    void eliminarCarritoProd(Long id);

    void eliminarCarritoProdIdUsuario(Long id);
    //METODOS incrementar y disminuir

    CarritoProd aumentarCantidad (Long id);

    CarritoProd diminuirCantidad (Long id);

    ///LISTAR TODO EL CARRITO
    List<CarritoProd> listarCarrito();

}
