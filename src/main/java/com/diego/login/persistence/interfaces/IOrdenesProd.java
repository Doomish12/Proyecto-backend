package com.diego.login.persistence.interfaces;

import com.diego.login.persistence.entity.OrdenesProd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IOrdenesProd {

    Page<OrdenesProd> listarOrdenes(Pageable pageable);

    Page<OrdenesProd> getOrdenesProd(Long id, Pageable pageable);

    Optional<OrdenesProd> getOrdenesProdById(Long id);

    OrdenesProd crearOrdenDesdeCarrito(Long userId);

    OrdenesProd actualizarPorEstadoOrden(Long id, OrdenesProd ordenesProd);

    void eliminarOrden(Long id);


}
