package com.diego.login.controller;

import com.diego.login.dto.RegisterUsuario;
import com.diego.login.dto.SaveMensaje;
import com.diego.login.dto.SaveUsuario;
import com.diego.login.dto.auth.AuthResponse;
import com.diego.login.dto.auth.LoginRequest;
import com.diego.login.dto.auth.VerificarEmailRequest;
import com.diego.login.dto.auth.VerificarEmailResponse;
import com.diego.login.exception.ObjectNotFoundException;
import com.diego.login.persistence.entity.Usuario;
import com.diego.login.persistence.repository.UsuarioRepo;
import com.diego.login.services.auth.JwtService;
import com.diego.login.services.impl.AuthService;
import com.diego.login.services.tienda.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")

public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private JwtService jwtService;


    @GetMapping("/validar-token")
    public ResponseEntity<AuthResponse> validarToken(@RequestHeader("Authorization") String authorizationHeader) {
        // Verificar que el encabezado Authorization contiene el prefijo "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extraer el token JWT del encabezado
        String token = authorizationHeader.substring(7);

        AuthResponse authResponse = authService.validateToken(token);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUsuario> registrarUsuario(@RequestBody @Valid SaveUsuario saveUsuario) {
        RegisterUsuario registerUsuario = authService.registerUsuario(saveUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUsuario);
    }


    @PostMapping("/verify-email")
    public ResponseEntity<VerificarEmailResponse> verifyEmail(@RequestBody VerificarEmailRequest request) {
        String email = request.getEmail();
        String codigo = request.getCodigo();

        Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);

        if (usuario != null) {
            if (usuario.getVerificacionCodigo().equals(codigo)) {
                if (usuario.getFechaCodigoVerficacion().plusMinutes(5).isAfter(LocalDateTime.now())) {
                    usuario.setEmailVerificacion(true);
                    usuarioRepo.save(usuario);
                    String jwt = jwtService.getToken(authService.generateExtraClaims(usuario),usuario);
                    return ResponseEntity.ok(new VerificarEmailResponse("Email verificado exitosamente. Tu cuenta está activa.", true, usuario,jwt));
                } else {
                    return ResponseEntity.badRequest().body(new VerificarEmailResponse("El código de verificación ha expirado y tu cuenta ha sido eliminada. Debes esperar 5 minutos antes de poder crear una nueva cuenta con el mismo correo.", false, null,null));
                }
            }
            return ResponseEntity.badRequest().body(new VerificarEmailResponse("Código de verificación inválido.", false,null,null));
        }

        return ResponseEntity.badRequest().body(new VerificarEmailResponse("Email no encontrado.", false,null,null));
    }

    //ENVIAR MENSAJE CORREO CONTACTO
    @PostMapping("/enviar-mensaje")
    public ResponseEntity<Map<String,String>> enviarMensaje(@RequestBody @Valid SaveMensaje mensaje){
        Map<String, String> response = new HashMap<>();
        try {
            emailService.sendEmailContacto(mensaje);
            response.put("mensaje", "Su mensaje se ha enviado exitosamente. Nos pondremos en contacto con usted pronto.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (ObjectNotFoundException e){
            response.put("error", "Error al enviar mensaje");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }


}
