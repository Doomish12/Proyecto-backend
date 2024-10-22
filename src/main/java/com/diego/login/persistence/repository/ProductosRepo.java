package com.diego.login.persistence.repository;

import com.diego.login.persistence.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductosRepo extends JpaRepository<Productos, Long> {
    List<Productos> findByCategoriaProd_Id(Long idCategoriaProd);

    //LISTAR POR LAS ORDENES MENORES A 10 QUERY
    @Query("SELECT p FROM Productos p WHERE p.stock_pro < :threshold")
    List<Productos> findByStock_proLessThan(@Param("threshold") int threshold);

    // Obtener los 5 productos más recientes basados en el ID
    @Query("SELECT p FROM Productos p ORDER BY p.id_producto DESC LIMIT 5")
    List<Productos> findTop5Productos();

    // LISTAR PRODUCTOS CON FILTRO OPCIONAL POR CATEGORÍA Y RANGO DE PRECIOS
    @Query("SELECT p FROM Productos p WHERE " +
            "(:minPrice IS NULL OR p.precio_pro >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.precio_pro <= :maxPrice) " +
            "AND (:categoriaId IS NULL OR p.categoriaProd.id = :categoriaId)")
    List<Productos> findByPrecioBetweenAndCategoriaId(
                                                @Param("minPrice") Double minPrice,
                                                @Param("maxPrice") Double maxPrice,
                                                @Param("categoriaId") Long categoriaId);


}
