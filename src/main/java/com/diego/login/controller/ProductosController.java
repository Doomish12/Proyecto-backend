package com.diego.login.controller;


import com.diego.login.dto.productos.SaveProductos;
import com.diego.login.exception.ObjectNotFoundException;
import com.diego.login.persistence.entity.Productos;
import com.diego.login.services.tienda.ProductosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductosController {

    @Autowired
    private ProductosService productosService;


    @GetMapping("/listarProductos")
    public ResponseEntity<Page<Productos>> listarProductos(Pageable pageable) throws InterruptedException {
       // Thread.sleep(500);
        Page<Productos> listar = productosService.getProductos(pageable);
        if(listar.hasContent()){
            return ResponseEntity.ok(listar);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/agregar")
    public ResponseEntity<Productos> agregarProducto(@RequestBody @Valid SaveProductos productos) {
        Productos productoSave = productosService.guardarProducto(productos);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoSave);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Productos> buscarProducto(@PathVariable Long id) {
        Optional<Productos> buscar = productosService.obtenerProductoId(id);
        if(buscar.isPresent()){
            return ResponseEntity.ok(buscar.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Productos> actualizarProducto(@PathVariable Long id, @RequestBody @Valid SaveProductos productos) {
        Productos productoSave = productosService.actualizarProducto(id, productos);
        return ResponseEntity.ok(productoSave);
    }

    @PutMapping("/actualizar-stock/{id}")
    public ResponseEntity<Productos> actualizarStock(@PathVariable Long id, @RequestBody Map<String, Integer> stockMap) {
        Integer stockPro = stockMap.get("stock_pro");
        if (stockPro == null) {
            throw new IllegalArgumentException("El stock no puede ser nulo");
        }
        Productos productoSave = productosService.actualizarProductoStock(id, stockPro);
        return ResponseEntity.ok(productoSave);
    }


    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminarProducto(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        try {
            productosService.eliminarProducto(id);
            response.put("mensaje", "El producto fue eliminado");
            return ResponseEntity.ok(response);
        } catch (ObjectNotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @GetMapping("/productos-bajos")
    public ResponseEntity<List<Productos>> getLowInventoryProducts() throws InterruptedException {
      //  Thread.sleep(2100);
        List<Productos> lowInventoryProducts = productosService.getProductosStockMenores();
        return new ResponseEntity<>(lowInventoryProducts, HttpStatus.OK);
    }

    @GetMapping("/productos-reciente")
    public ResponseEntity<List<Productos>> getProductosReciente() throws InterruptedException {
      //  Thread.sleep(2100);
        List<Productos> productosReciente = productosService.getProductoRecienAgregado();
        return new ResponseEntity<>(productosReciente, HttpStatus.OK);
    }


    @GetMapping("/filtrarPorPrecio")
    public ResponseEntity<List<Productos>> filtrarPorPrecio(@RequestParam(required = false) Double minPrice,
                                                            @RequestParam(required = false) Double maxPrice,
                                                            @RequestParam(required = false) Long categoriaId) throws InterruptedException {
     //   Thread.sleep(500);
        List<Productos> productos = productosService.getProductosRangoPrecio(minPrice,maxPrice, categoriaId);
        return ResponseEntity.ok(productos);
    }
}

