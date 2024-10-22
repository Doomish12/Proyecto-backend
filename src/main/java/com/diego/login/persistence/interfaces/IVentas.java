package com.diego.login.persistence.interfaces;

import com.diego.login.persistence.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVentas {

    Page<Venta> getHistorialVentas(Pageable pageable);

    Page<Venta> getHistorialVentasPorUsuario(Long id,Pageable pageable);

    void eliminarHistorialVentas(Long id);
}
