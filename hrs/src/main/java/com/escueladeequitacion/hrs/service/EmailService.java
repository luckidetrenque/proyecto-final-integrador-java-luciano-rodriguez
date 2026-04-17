package com.escueladeequitacion.hrs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:escuelahrs.test@gmail.com}")
    private String fromEmail;

    public void enviarInvitacion(String to, String nombreAlumno, String linkRegistro) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("¡Bienvenido a HRS Escuela de Equitación! - Invitación de Registro");

            String htmlContent = String.format(
                "<html><body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>" +
                "<h2 style='color: #2c3e50;'>Hola %s,</h2>" +
                "<p>Nos alegra mucho saludarte desde la <strong>Escuela de Equitación HRS</strong>.</p>" +
                "<p>Para completar tu registro en nuestro sistema y acceder a tus clases, reservas y facturación, por favor hacé clic en el siguiente botón:</p>" +
                "<div style='text-align: center; margin: 30px 0;'>" +
                "<a href='%s' style='background-color: #3b82f6; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Completar Registro</a>" +
                "</div>" +
                "<p>Si el botón no funciona, podés copiar y pegar este enlace en tu navegador:</p>" +
                "<p>%s</p>" +
                "<p>Este enlace es válido por 7 días.</p>" +
                "<br><p>¡Te esperamos pronto en las pistas!</p>" +
                "<hr><p style='font-size: 0.8em; color: #7f8c8d;'>Este es un mensaje automático, por favor no lo respondas.</p>" +
                "</body></html>",
                nombreAlumno, linkRegistro, linkRegistro
            );

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de invitación", e);
        }
    }
}
