package com.example.public_service_management.common.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailUtil {
  private final JavaMailSender mailSender;

  public void sendSimpleMail(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    mailSender.send(message);
  }

  public void sendHtmlMail(String to, String subject, String htmlContent) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);

      mailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  @Async
  public void sendSimpleMailAsync(String to, String subject, String text) {
    sendSimpleMail(to, subject, text);
  }

  @Async
  public void sendHtmlMailAsync(String to, String subject, String htmlContent) {
    sendHtmlMail(to, subject, htmlContent);
  }
}
