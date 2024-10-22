package com.diego.login.persistence.repository;

import com.diego.login.persistence.entity.ListaDeseosProd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaDeseosProdRepo extends JpaRepository<ListaDeseosProd, Long> {

    Page<ListaDeseosProd>  findByUsuarioId(Long idUsuario, Pageable pageable);

}
