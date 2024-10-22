package com.diego.login.persistence.repository;

import com.diego.login.dto.CategoriaProdCount;
import com.diego.login.persistence.entity.CategoriaProd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoriaRepo extends  JpaRepository<CategoriaProd,Long>{

    //LISTAR CANTIDAD DE PRODUCTOS QUE TIENE CADA CATEGORIA.
    @Query("SELECT c.nombre_categoria_prod AS categoria, COUNT(p.id_producto) AS cantidadProductos " +
            "FROM Productos p " +
            "JOIN p.categoriaProd c " +
            "GROUP BY c.nombre_categoria_prod")
    List<CategoriaProdCount> findCategoriaProductoCounts();
}
