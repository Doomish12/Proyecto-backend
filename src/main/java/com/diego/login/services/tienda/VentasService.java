package com.diego.login.services.tienda;

import com.diego.login.persistence.entity.Venta;
import com.diego.login.persistence.interfaces.IVentas;
import com.diego.login.persistence.repository.VentasRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VentasService implements IVentas {

    @Autowired
    private VentasRepo ventasRepo;

    @Override
    public Page<Venta> getHistorialVentas(Pageable pageable) {
        return ventasRepo.findAll(pageable);
    }

    @Override
    public Page<Venta> getHistorialVentasPorUsuario(Long id, Pageable pageable) {
        return ventasRepo.findByUsuarioId(id, pageable);
    }

    @Override
    public void eliminarHistorialVentas(Long id) {
        ventasRepo.deleteById(id);
    }
}
