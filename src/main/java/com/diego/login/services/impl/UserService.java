package com.diego.login.services.impl;

import com.diego.login.dto.SaveUsuario;
import com.diego.login.dto.UpdateUsuario;
import com.diego.login.dto.UsuarioDTO;
import com.diego.login.exception.InvalidPasswordException;
import com.diego.login.exception.ObjectNotFoundException;
import com.diego.login.exception.registerPersonalizadoException;
import com.diego.login.persistence.entity.Usuario;
import com.diego.login.persistence.interfaces.IUsuario;
import com.diego.login.persistence.repository.UsuarioRepo;
import com.diego.login.persistence.util.EstadoUsuario;
import com.diego.login.persistence.util.Rol;
import com.diego.login.services.tienda.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService  implements IUsuario {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;


    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepo.findAll(pageable);
    }

    @Override
    public void eliminarUsuario(Long id) {
     try{
         usuarioRepo.deleteById(id);
     }
     catch (DataIntegrityViolationException e){
         throw new ObjectNotFoundException("No se puede eliminar el usuario porque está en uso en otras entidades.");
     }
     catch (Exception e){
         throw new ObjectNotFoundException("Ocurrió un error al intentar eliminar el usuario");
     }
    }


    //PARA LOS ADMINS
    @Override
    public Usuario registarUsuarioAdmin(UpdateUsuario updateUsuario) {

        // Verificar si el username ya está en uso por otro usuario
        if (usuarioRepo.existsByUsername(updateUsuario.getUsername())) {
            throw new registerPersonalizadoException("Ya existe un usuario con el username '" + updateUsuario.getUsername() + "'");
        }

        // Verificar si el email ya está en uso por otro usuario
        if (usuarioRepo.existsByEmail(updateUsuario.getEmail())) {
            throw new registerPersonalizadoException("Ya existe un usuario con el email '" + updateUsuario.getEmail() + "'");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(updateUsuario.getNombre());
        usuario.setApellido(updateUsuario.getApellido());
        usuario.setUsername(updateUsuario.getUsername());
        usuario.setEmail(updateUsuario.getEmail());
        usuario.setPassword(passwordEncoder.encode(updateUsuario.getPassword()));
        usuario.setRol(updateUsuario.getRol());
        usuario.setEstado(EstadoUsuario.INACTIVO);
        //GENERA CODIGO
        String verificationCode = String.valueOf(1000 + new Random().nextInt(9000));
        usuario.setVerificacionCodigo(verificationCode);
        usuario.setFechaCodigoVerficacion(LocalDateTime.now());
        usuario.setEmailVerificacion(true);

        return  usuarioRepo.save(usuario);
    }


    public Optional<UsuarioDTO> findById(Long id) {
        Optional<Usuario> usuario = usuarioRepo.findById(id);
        return usuario.map(this::convertToDto);
    }

    //OBTENER USUARIO SIN EL PASSWORD
    private UsuarioDTO convertToDto(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getImagen_user()
        );
    }

    //PARA LOS USUARIOS NORMALES
    @Override
    public Usuario registrarUsuario(SaveUsuario saveUsuario) {

        // Verificar si el username ya está en uso por otro usuario
        if (usuarioRepo.existsByUsername(saveUsuario.getUsername())) {
            throw new registerPersonalizadoException("Ya existe un usuario con el username '" + saveUsuario.getUsername() + "'");
        }

        // Verificar si el email ya está en uso por otro usuario
        if (usuarioRepo.existsByEmail(saveUsuario.getEmail())) {
            throw new registerPersonalizadoException("Ya existe un usuario con el email '" + saveUsuario.getEmail() + "'");
        }


        //VALIDATE LLAMA AL METODO QUE VALIDA EL PASSWORD 2 VECES
        validatePassword(saveUsuario);

        Usuario usuario = new Usuario();
        usuario.setUsername(saveUsuario.getUsername());
        usuario.setNombre(saveUsuario.getNombre());
        usuario.setApellido(saveUsuario.getApellido());
        usuario.setEmail(saveUsuario.getEmail());
        usuario.setPassword(passwordEncoder.encode(saveUsuario.getPassword()));
        usuario.setRol(Rol.USUARIO);
        usuario.setEstado(EstadoUsuario.INACTIVO);
        // Generar código de verificación
        String verificationCode = String.valueOf(1000 + new Random().nextInt(9000));
        usuario.setVerificacionCodigo(verificationCode);
        usuario.setFechaCodigoVerficacion(LocalDateTime.now());

        // Enviar el correo de verificación
        emailService.sendVerificationEmail(usuario.getEmail(), verificationCode);

        return usuarioRepo.save(usuario);
    }


    @Override
    public Usuario actualizarUsuario(Long id, UpdateUsuario updateUsuario) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado"));

        // Verificar si el nuevo username ya está en uso por otro usuario
        if (!usuario.getUsername().equals(updateUsuario.getUsername()) && usuarioRepo.existsByUsername(updateUsuario.getUsername())) {
            throw new registerPersonalizadoException("Ya existe un usuario con el username '" + updateUsuario.getUsername() + "'");
        }

        // Verificar si el nuevo email ya está en uso por otro usuario
        if (!usuario.getEmail().equals(updateUsuario.getEmail()) && usuarioRepo.existsByEmail(updateUsuario.getEmail())) {
            throw new registerPersonalizadoException("Ya existe un usuario con el email '" + updateUsuario.getEmail() + "'");
        }

        //ACTUALIZAR USUARIO
        usuario.setUsername(updateUsuario.getUsername());
        usuario.setNombre(updateUsuario.getNombre());
        usuario.setApellido(updateUsuario.getApellido());
        usuario.setEmail(updateUsuario.getEmail());
        usuario.setPassword(passwordEncoder.encode(updateUsuario.getPassword()));
        usuario.setRol(updateUsuario.getRol());
        usuario.setImagen_user(updateUsuario.getImagen_user());

        Usuario usuarioActualizado = usuarioRepo.save(usuario);

        return usuarioActualizado;
    }



    //VALIDACION DE PASSWORD PARA 2 VECES ESCRIBIRLA
    private void validatePassword(SaveUsuario dto) {

        if(!StringUtils.hasText(dto.getPassword()) || !StringUtils.hasText(dto.getRepeatPassword())){
            throw  new InvalidPasswordException("Las contraseñas no coinciden");
        }

        if(!dto.getPassword().equals(dto.getRepeatPassword())){
            throw  new InvalidPasswordException("Las contraseñas no coinciden");
        }
    }

}
