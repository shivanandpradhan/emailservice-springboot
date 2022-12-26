package com.project.emailservice.services;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.project.emailservice.entity.EmailDetails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService implements IEmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    final String SUCCESS_MAIL_MESSAGE = "Mail Sent Successfully...";
    final String ERROR_MAIL_MESSAGE = "Error while sending mail!!!";

    /**
     * sending simple email with text to recipient.
     */
    @Override
    public String sendSimpleMail(EmailDetails emailDetails) {
        log.info(String.format("Sending Simple Mail to : %s", emailDetails.getRecipient()));

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(emailDetails.getRecipient());
        simpleMailMessage.setSubject(emailDetails.getSubject());
        simpleMailMessage.setText(emailDetails.getMessageBody());
        try {
            javaMailSender.send(simpleMailMessage);
            log.info(String.format("Mail Sent Successfully to : %s", emailDetails.getRecipient()));
        } catch (MailException mailException) {
            log.error(mailException.getMessage(), mailException);
            return ERROR_MAIL_MESSAGE;
        }
        return SUCCESS_MAIL_MESSAGE;
    }

    /**
     * sending mail with attachment to the recipient.
     */
    @Override
    public String sendMailWithAttachments(EmailDetails emailDetails) {
        log.info(String.format("Sending AttachMent Mail to : %s", emailDetails.getRecipient()));

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // Setting multipart as true for attachments to be send
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMessageBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            // Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            log.info(String.format("Mail Sent Successfully to : %s", emailDetails.getRecipient()));
        } catch (MessagingException messagingException) {
            log.error(messagingException.getMessage(), messagingException);
            return ERROR_MAIL_MESSAGE;
        }
        return SUCCESS_MAIL_MESSAGE;
    }

    @Override
    public String sendMailUsingThymeleaf(EmailDetails emailDetails) throws MessagingException {
        log.info(String.format("Sending Mail to : %s using thymeleaf", emailDetails.getRecipient()));
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        // setting all variables
        // context.setVariables(emailDetails.getProperties());

        // set variable - will use in template
        context.setVariable("name", "Shivanand Pradhan");

        helper.setFrom(sender);
        helper.setTo(emailDetails.getRecipient());
        helper.setSubject(emailDetails.getSubject());

        // getting mail template to send.
        String html = templateEngine.process(emailDetails.getTemplate(), context);
        helper.setText(html, true);

        //sending mail
        javaMailSender.send(mimeMessage);
        log.info(String.format("Mail Sent Successfully to : %s", emailDetails.getRecipient()));
        return SUCCESS_MAIL_MESSAGE;
    }
}