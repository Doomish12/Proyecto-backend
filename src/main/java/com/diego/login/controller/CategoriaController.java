package com.diego.login.controller;


import com.diego.login.dto.CategoriaProdCount;
import com.diego.login.persistence.entity.CategoriaProd;
import com.diego.login.services.tienda.CategoriaProService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/categoria")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaProService categoriaProService;


    @GetMapping("/listarCategoria")
    public ResponseEntity<Page<CategoriaProd>> listarCategoria(Pageable pageable) {

        Page<CategoriaProd> categoria = categoriaProService.getCategorias(pageable);

        if(categoria.hasContent()){
            return ResponseEntity.ok(categoria);
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PostMapping("/guardar")
    public ResponseEntity<CategoriaProd> guardar(@RequestBody @Valid CategoriaProd categoria) {
        CategoriaProd categoriaProd = categoriaProService.guardarCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaProd);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<CategoriaProd> buscar(@PathVariable Long id) {
        Optional<CategoriaProd> obtenerCategoria = categoriaProService.obtenerCategoriaId(id);

        if(obtenerCategoria.isPresent()){
            return ResponseEntity.ok(obtenerCategoria.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<CategoriaProd> actualizar(@PathVariable Long id, @RequestBody @Valid CategoriaProd categoria) {
        CategoriaProd categoriaProd =  categoriaProService.actualizarCategoria(id, categoria);
        return ResponseEntity.ok(categoriaProd);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        categoriaProService.eliminarCategoria(id);
        return ResponseEntity.ok("Categoria Eliminado");
    }

    @GetMapping("/cantidad-productos")
    public ResponseEntity<List<CategoriaProdCount>> listarCategoriaCantidadProd() {
        List<CategoriaProdCount> lista = categoriaProService.getCategoriaProductoCounts();
        return ResponseEntity.ok(lista);
    }
}
