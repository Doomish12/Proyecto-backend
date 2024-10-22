package com.diego.login.controller;

import com.diego.login.persistence.entity.Venta;
import com.diego.login.services.tienda.VentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/historialVentas")
@CrossOrigin(origins = "*")
public class VentasController {

    @Autowired
    private VentasService ventasService;

    @GetMapping("/listarHistorial")
    public ResponseEntity<Page<Venta>> listarHistorial(Pageable pageable) {
        Page<Venta> listar = ventasService.getHistorialVentas(pageable);

        if(listar.hasContent()){
            return ResponseEntity.ok(listar);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/listarHistorialUsuario/{id}")
    public ResponseEntity<Page<Venta>> listarHistorial(@PathVariable Long id, Pageable pageable) {
        Page<Venta> listar = ventasService.getHistorialVentasPorUsuario(id,pageable);

        if(listar.hasContent()){
            return ResponseEntity.ok(listar);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        ventasService.eliminarHistorialVentas(id);
        return ResponseEntity.ok().body("El historial ha sido eliminado");
    }


}
