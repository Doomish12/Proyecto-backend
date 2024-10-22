package com.diego.login.persistence.repository;

import com.diego.login.persistence.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentasRepo extends JpaRepository<Venta, Long> {

    //LISTAR POR USUARIO PAGEABLE
    Page<Venta> findByUsuarioId(Long id, Pageable pageable);

    //LISTAR POR USUARIO SIMPLE LISTADO
    List<Venta> findByUsuarioId(Long id);


}
