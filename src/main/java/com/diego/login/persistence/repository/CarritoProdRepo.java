package com.diego.login.persistence.repository;

import com.diego.login.persistence.entity.CarritoProd;
import com.diego.login.persistence.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarritoProdRepo extends JpaRepository<CarritoProd, Long> {

    Page<CarritoProd> findByUsuarioId(Long id, Pageable pageable);

    void deleteByUsuarioId(Long id);

    List<CarritoProd> findByUsuarioId(Long idUsuario);

}
