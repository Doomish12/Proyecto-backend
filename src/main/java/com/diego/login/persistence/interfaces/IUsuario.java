package com.diego.login.persistence.interfaces;

import com.diego.login.dto.SaveUsuario;
import com.diego.login.dto.UpdateUsuario;
import com.diego.login.dto.UsuarioDTO;
import com.diego.login.persistence.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUsuario {

    Page<Usuario> findAll(Pageable pageable);

    Optional<UsuarioDTO> findById(Long id);

    Usuario registrarUsuario(SaveUsuario saveUsuario);

    Usuario registarUsuarioAdmin(UpdateUsuario updateUsuario);

    Usuario actualizarUsuario(Long id, UpdateUsuario updateUsuario);

    void eliminarUsuario(Long id);

}
