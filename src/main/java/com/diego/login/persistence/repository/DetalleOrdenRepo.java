package com.diego.login.persistence.repository;

import com.diego.login.persistence.entity.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DetalleOrdenRepo extends JpaRepository<DetalleOrden, Long> {
   List<DetalleOrden> findByOrdenesProdId(Long id);
}
