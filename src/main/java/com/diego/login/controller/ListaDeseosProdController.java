package com.diego.login.controller;

import com.diego.login.dto.productosReponse.ListaDeseoReponse;
import com.diego.login.dto.productos.SaveListaDeseos;
import com.diego.login.dto.productosReponse.ListaDeseoResponseGet;
import com.diego.login.persistence.entity.ListaDeseosProd;
import com.diego.login.services.tienda.ListaDeseosProdService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listaDeseos")
@CrossOrigin(origins = "*")
public class ListaDeseosProdController {

    @Autowired
    private ListaDeseosProdService listaDeseosProdService;


    @GetMapping("/listaDeseos/{idUser}")
    public ResponseEntity<Page<ListaDeseoResponseGet>> getListaDeseos(Pageable pageable, @PathVariable Long idUser) throws InterruptedException {
        Page<ListaDeseoResponseGet> lista = listaDeseosProdService.getListaDeseosProd(pageable, idUser);

        if(lista.hasContent()){
            return ResponseEntity.ok(lista);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/agregar")
    public ResponseEntity<ListaDeseoReponse> guardar(@RequestBody @Valid SaveListaDeseos listaDeseos) {
        ListaDeseoReponse guardar = listaDeseosProdService.guardarListaDeseosProd(listaDeseos);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardar);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        listaDeseosProdService.eliminarListaDeseoProd(id);
        return ResponseEntity.ok().body("Eliminado el prod de lista de deseo");
    }

}
