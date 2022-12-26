package com.project.emailservice.services;

import com.project.emailservice.entity.EmailDetails;

import jakarta.mail.MessagingException;

public interface IEmailService {
    String sendSimpleMail(EmailDetails emailDetails);
    String sendMailWithAttachments(EmailDetails emailDetails);
    String sendMailUsingThymeleaf(EmailDetails emailDetails) throws MessagingException;
}
