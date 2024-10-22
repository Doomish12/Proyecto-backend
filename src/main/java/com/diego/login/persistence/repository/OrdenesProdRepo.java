package com.diego.login.persistence.repository;

import com.diego.login.persistence.entity.OrdenesProd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdenesProdRepo extends JpaRepository<OrdenesProd, Long> {

    Page<OrdenesProd> findByUsuarioId(Long id, Pageable pageable);

    List<OrdenesProd> findByUsuarioId(Long id);

    //LISTAR POR FECHA DE EXPIRACIÓN
    List<OrdenesProd> findByFechaExpiracionBefore(LocalDateTime now);

}
