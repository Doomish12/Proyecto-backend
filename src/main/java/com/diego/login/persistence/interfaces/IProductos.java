package com.diego.login.persistence.interfaces;

import com.diego.login.dto.productos.SaveProductos;
import com.diego.login.persistence.entity.Productos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProductos {

    Page<Productos> getProductos(Pageable pageable);

    Optional<Productos> obtenerProductoId(Long id);

    Productos guardarProducto(SaveProductos producto);

    Productos actualizarProducto(Long id,SaveProductos producto);

    Productos actualizarProductoStock(Long id,int stock);

    void eliminarProducto(Long id);

}
