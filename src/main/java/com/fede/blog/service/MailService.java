package com.fede.blog.service;

import com.fede.blog.exceptions.BlogException;
import com.fede.blog.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {


    private final JavaMailSender mailSender;
    private final String sender;

    /*
    @Async
    void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("blog@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new BlogException("Exception occurred when sending mail to " + notificationEmail.getRecipient(), e);
        }
    }

     */
    @Async
    void sendMail(NotificationEmail notificationEmail) {
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();

        // Setting up necessary details
        mailMessage.setFrom(sender);
        mailMessage.setTo(notificationEmail.getRecipient());
        mailMessage.setText(notificationEmail.getBody());
        mailMessage.setSubject(notificationEmail.getSubject());
        try {
            mailSender.send(mailMessage);
            log.info("Mail Sent Successfully.");
        } catch (MailException e) {
            log.info("Error while Sending Mail: ", e);
            throw new BlogException("Exception occurred when sending mail to " + notificationEmail.getRecipient(), e);
        }
    }
}