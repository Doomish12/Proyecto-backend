package com.diego.login.persistence.interfaces;

import com.diego.login.persistence.entity.CategoriaProd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICategoriaProd {

    Page<CategoriaProd> getCategorias(Pageable pageable);

    Optional<CategoriaProd> obtenerCategoriaId(Long id_Categoria);

    CategoriaProd guardarCategoria(CategoriaProd categoria);

    CategoriaProd actualizarCategoria(Long id_Categoria,CategoriaProd categoria);

    void eliminarCategoria(Long id_Categoria);
}
