package com.diego.login.persistence.interfaces;

import com.diego.login.dto.productosReponse.ListaDeseoReponse;
import com.diego.login.dto.productos.SaveListaDeseos;
import com.diego.login.dto.productosReponse.ListaDeseoResponseGet;
import com.diego.login.persistence.entity.ListaDeseosProd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IListaDeseosProd {

    Page<ListaDeseoResponseGet> getListaDeseosProd(Pageable pageable, Long userId);

    ListaDeseoReponse guardarListaDeseosProd(SaveListaDeseos listaDeseos);

    void eliminarListaDeseoProd(Long id);


}
