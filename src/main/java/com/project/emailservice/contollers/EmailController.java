package com.project.emailservice.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.emailservice.entity.EmailDetails;
import com.project.emailservice.services.IEmailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/mail")
public class EmailController {

    @Autowired
    IEmailService emailService;
    
    @PostMapping("/send-mail")
    public String sendSimpleEmail(@RequestBody EmailDetails emailDetails){
        return emailService.sendSimpleMail(emailDetails);
    }

    @PostMapping("/send-mail/attachment")
    public String sendMailWithAttachments(@RequestBody EmailDetails emailDetails){
        return emailService.sendMailWithAttachments(emailDetails);
    }

    @PostMapping("/send-mail/thymeleaf")
    public String sendMailUsingThymeleaf(@RequestBody EmailDetails emailDetails) throws MessagingException{
        return emailService.sendMailUsingThymeleaf(emailDetails);
    }
}

