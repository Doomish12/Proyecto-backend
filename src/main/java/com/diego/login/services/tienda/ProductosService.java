package com.diego.login.services.tienda;

import com.diego.login.dto.productos.SaveProductos;
import com.diego.login.exception.ObjectNotFoundException;
import com.diego.login.persistence.entity.CategoriaProd;
import com.diego.login.persistence.entity.Productos;
import com.diego.login.persistence.interfaces.IProductos;
import com.diego.login.persistence.repository.CategoriaRepo;
import com.diego.login.persistence.repository.ProductosRepo;
import com.diego.login.persistence.util.EstadoCategoria;
import com.diego.login.persistence.util.EstadoOrdenProd;
import com.diego.login.persistence.util.EstadoProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductosService implements IProductos {

    @Autowired
    private ProductosRepo productosRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Override
    public Page<Productos> getProductos(Pageable pageable) {
        return productosRepo.findAll(pageable);
    }

    @Override
    public Optional<Productos> obtenerProductoId(Long id) {
        return  productosRepo.findById(id);
    }

    //LISTAR PRODUCTO MENORES A 10 DE STOCK
    public List<Productos> getProductosStockMenores() {
        int stock = 10;
        return productosRepo.findByStock_proLessThan(stock);
    }
    //LISTAR LOS PRIMEROS 5 PRODUCTOS AGREGADOS
    public List<Productos> getProductoRecienAgregado() {
        return productosRepo.findTop5Productos();
    }

    // LISTAR PRODUCTOS CON FILTRO OPCIONAL POR CATEGORÍA Y RANGO DE PRECIOS
    public List<Productos> getProductosRangoPrecio(Double minPrice, Double maxPrice,Long categoriaId) {
        return productosRepo.findByPrecioBetweenAndCategoriaId(minPrice,maxPrice,categoriaId);
    }

    @Override
    public Productos guardarProducto(SaveProductos producto) {

        Productos productos = new Productos();
        //BUSCAR EL LA CATEGORIA POR ID
        CategoriaProd categoriaProd = categoriaRepo.findById(producto.getId_categoria_prod())
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        CategoriaProd categoria = new CategoriaProd();
        categoria.setId(producto.getId_categoria_prod());
        categoria.setNombre_categoria_prod("Se visualizara el nombre en el listado");
        categoria.setEstado_categoria(EstadoCategoria.ACTIVO);

        productos.setNombre_pro(producto.getNombre_pro());
        productos.setDescripcion_pro(producto.getDescripcion_pro());
        productos.setPrecio_pro(producto.getPrecio_pro());
        productos.setImagen_pro(producto.getImagen_pro());
        productos.setStock_pro(producto.getStock_pro());
        productos.setEstado_pro(EstadoProducto.valueOf(producto.getEstado_pro()));
        productos.setCategoriaProd(categoria);

        // Actualiza el estado de la categoría a activo , CUANDO EL PRODUCTO SE INSERTA
        categoriaProd.setEstado_categoria(EstadoCategoria.ACTIVO);
        categoriaRepo.save(categoriaProd);
        return productosRepo.save(productos);
    }

    @Override
    public Productos actualizarProducto(Long id, SaveProductos producto) {
        Productos productos = productosRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("No se encontró el producto con id: " + id));

        CategoriaProd categoriaAnterior = productos.getCategoriaProd();
        CategoriaProd nuevaCategoria = categoriaRepo.findById(producto.getId_categoria_prod())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Actualizamos solo los campos del producto
        productos.setNombre_pro(producto.getNombre_pro());
        productos.setDescripcion_pro(producto.getDescripcion_pro());
        productos.setPrecio_pro(producto.getPrecio_pro());
        productos.setImagen_pro(producto.getImagen_pro());
        productos.setStock_pro(producto.getStock_pro());
        productos.setEstado_pro(EstadoProducto.valueOf(producto.getEstado_pro()));
        productos.setCategoriaProd(nuevaCategoria);

        Productos productoActualizado = productosRepo.save(productos);

        // Verifica si la categoría anterior debe ser actualizada
        if (!categoriaAnterior.equals(nuevaCategoria)) {
            actualizarEstadoCategoria(categoriaAnterior);
            actualizarEstadoCategoria(nuevaCategoria);
        }

        return productoActualizado;
    }

    @Override
    public Productos actualizarProductoStock(Long id, int stockPro) {
        Productos productos = productosRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("No se encontró el producto con id: " + id));

        productos.setStock_pro(stockPro);
        if(stockPro >= 10){
            productos.setEstado_pro(EstadoProducto.EN_STOCK);
        }else{
            productos.setEstado_pro(EstadoProducto.POCO_STOCK);
        }

        return productosRepo.save(productos);
    }

    @Override
    public void eliminarProducto(Long id) {
        //BUSCAR PRODUCTO ID
        Productos producto = productosRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CategoriaProd categoria = producto.getCategoriaProd();
        try{
            //ELIMINAR PRODUCTO
            productosRepo.deleteById(id);
            actualizarEstadoCategoria(categoria);
        }
        catch (DataIntegrityViolationException e){
            throw new ObjectNotFoundException("No se puede eliminar el producto porque está en uso en otras entidades.");
        }
        catch (Exception e){
            throw new ObjectNotFoundException("Ocurrió un error al intentar eliminar el producto.");
        }


    }

    private void actualizarEstadoCategoria(CategoriaProd categoria) {
        List<Productos> productosRestantes = productosRepo.findByCategoriaProd_Id(categoria.getId());
        if (productosRestantes.isEmpty()) {
            categoria.setEstado_categoria(EstadoCategoria.NO_ACTIVO);
        } else {
            categoria.setEstado_categoria(EstadoCategoria.ACTIVO);
        }
        categoriaRepo.save(categoria);
    }

}
