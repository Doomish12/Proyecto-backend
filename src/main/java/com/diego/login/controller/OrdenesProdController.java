package com.diego.login.controller;

import com.diego.login.persistence.entity.OrdenesProd;
import com.diego.login.services.tienda.OrdenesProdService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/ordenes")
@CrossOrigin(origins = "*")
public class OrdenesProdController {

    @Autowired
    private OrdenesProdService ordenesProdService;

    @GetMapping("/listarOrdenes")
    public ResponseEntity<Page<OrdenesProd>> listarOrdenes( Pageable pageable) {
        Page<OrdenesProd> lista = ordenesProdService.listarOrdenes(pageable);
        if(lista.hasContent()) {
            return ResponseEntity.ok(lista);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/listarOrdenUser/{id}")
    public ResponseEntity<Page<OrdenesProd>> listarOrdenes(@PathVariable Long id, Pageable pageable) {
        Page<OrdenesProd> lista = ordenesProdService.getOrdenesProd(id,pageable);
        if(lista.hasContent()) {
            return ResponseEntity.ok(lista);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<OrdenesProd> listarOrdenes(@PathVariable Long id) {
        Optional<OrdenesProd> buscar = ordenesProdService.getOrdenesProdById(id);
        if(buscar.isPresent()) {
            return ResponseEntity.ok(buscar.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/crear/{userId}")
    public ResponseEntity<OrdenesProd> crearOrden(@PathVariable Long userId) {
        OrdenesProd guardar = ordenesProdService.crearOrdenDesdeCarrito(userId);
        return ResponseEntity.ok(guardar);
    }

    @PutMapping("/actualizar/{id}" )
    public ResponseEntity<?> actualizarEstadoOrden(@PathVariable Long id, @RequestBody @Valid OrdenesProd ordenesProd) {
        OrdenesProd actualizar = ordenesProdService.actualizarPorEstadoOrden(id, ordenesProd);
        return ResponseEntity.status(HttpStatus.OK).body(actualizar.getEstadoOrdenProd());
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarOrden(@PathVariable Long id) {
        ordenesProdService.eliminarOrden(id);
        return ResponseEntity.ok().body("Orden eliminado exitosamente");
    }



}
