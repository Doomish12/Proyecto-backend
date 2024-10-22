package com.diego.login.controller;

import com.diego.login.dto.productos.SaveCarritoProd;
import com.diego.login.dto.productosReponse.CarritoProdResponse;
import com.diego.login.dto.productosReponse.CarritoProdResponseGet;
import com.diego.login.exception.ObjectNotFoundException;
import com.diego.login.persistence.entity.CarritoProd;
import com.diego.login.services.tienda.CarritoProdService;
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

@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "*")
public class CarritoProdController {

    @Autowired
    private CarritoProdService carritoProdService;

    @GetMapping("/listarCarrito")
    public ResponseEntity<List<CarritoProd>> listarCarrito() {
        List<CarritoProd>  listar = carritoProdService.listarCarrito();
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    @GetMapping("/listarProductos/{id}")
    public ResponseEntity<Page<CarritoProdResponseGet>> getCarritoProds(@PathVariable Long id, Pageable pageable) {
        Page<CarritoProdResponseGet> listar = carritoProdService.getCarritoProds(id, pageable);

        if(listar.hasContent()){
            return ResponseEntity.ok(listar);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/agregarProducto")
    public ResponseEntity<?> guardarProducto(@RequestBody @Valid SaveCarritoProd saveCarritoProd) {
        CarritoProdResponse guardar = carritoProdService.guardar(saveCarritoProd);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardar);
    }



    @PutMapping("/aumentarCantidad/{id}")
    public ResponseEntity<Map<String, String>> aumentarCantidad(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            carritoProdService.aumentarCantidad(id);
            response.put("mensaje", "Cantidad actualizada correctamente");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ObjectNotFoundException e) {
            response.put("mensaje", e.getMessage()); // Retornar el mensaje de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/disminuirCantidad/{id}")
    public ResponseEntity<Map<String, Object>> disminuirCantidad(@PathVariable Long id) {
        CarritoProd producto =  carritoProdService.diminuirCantidad(id);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Cantidad disminuida correctamente");
        if(producto == null){
            response.put("eliminado", true);
        }
        else{
            response.put("eliminado", false);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/eliminarCarritoId/{id}")
    public ResponseEntity<String> eliminarCarritoId(@PathVariable Long id) {
        carritoProdService.eliminarCarritoProd(id);
        return ResponseEntity.ok().body("producto eliminado del carrito");
    }

    @DeleteMapping("/eliminarCarritoIdUsuario/{id}")
    public ResponseEntity<String> eliminarCarritoIdUsuario(@PathVariable Long id) {
        carritoProdService.eliminarCarritoProdIdUsuario(id);
        return ResponseEntity.ok().body("Se elimino todos los productos del usuario con el id:" + id);
    }

}
