package com.diego.login.persistence.repository;

import com.diego.login.persistence.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepo extends JpaRepository<DetalleVenta, Long> {
}
