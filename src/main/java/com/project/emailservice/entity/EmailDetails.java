package com.project.emailservice.entity;

import lombok.Data;

@Data
public class EmailDetails {
    private String recipient;
    private String subject;
    private String messageBody;
    private String attachment;   
    private String template;
}
