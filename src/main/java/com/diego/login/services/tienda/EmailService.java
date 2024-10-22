package com.diego.login.services.tienda;

import com.diego.login.dto.SaveMensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendVerificationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verificación de Email");
        message.setText("Para verificar tu email, usa el siguiente código: " + code);
        mailSender.send(message);
    }

    public void sendEmailContacto(SaveMensaje mensaje){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("innovatechxcorp@gmail.com");
        message.setSubject("Mensaje de contacto de " + mensaje.getNombre());
        message.setText("Nombre: " + mensaje.getNombre() + "\n" + "Email: " + mensaje.getEmail() +"\n" + "Mensaje: " +mensaje.getMensaje());
        mailSender.send(message);
    }


}
