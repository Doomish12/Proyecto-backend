package com.diego.login.controller;

import com.diego.login.dto.SaveMensaje;
import com.diego.login.dto.UpdateUsuario;
import com.diego.login.dto.UsuarioDTO;
import com.diego.login.exception.ObjectNotFoundException;
import com.diego.login.persistence.entity.Usuario;
import com.diego.login.services.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class UsuarioController {

    @Autowired
    private UserService userService;


    @GetMapping("/listaUsers")
    public ResponseEntity<Page<Usuario>> listarUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = userService.findAll(pageable);
        if(usuarios.hasContent()){
            return ResponseEntity.ok(usuarios);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioDTO> listarUsuarios(@PathVariable Long id) {
        Optional<UsuarioDTO> usuario = userService.findById(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody @Valid UpdateUsuario usuario) {
        Usuario usuarioActualizado = userService.actualizarUsuario(id,usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PostMapping("/agregar")
    public ResponseEntity<Usuario> agregarUsuario(@RequestBody @Valid UpdateUsuario usuario) {
        Usuario user = userService.registarUsuarioAdmin(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        try {
            userService.eliminarUsuario(id);
            response.put("mensaje", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        }catch (ObjectNotFoundException e){
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }





}
