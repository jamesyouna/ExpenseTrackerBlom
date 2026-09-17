package com.BLOM.expensetrackerapi.impls;

import com.BLOM.expensetrackerapi.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender; //Spring object that communicates with email server

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationCode(String email, String code) {
        //creates the mail
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Expense Tracker - Email Verification");
        message.setText(
                "Your verification code is: " + code +
                        "\n\nThis code expires in 10 minutes."
        );

        mailSender.send(message);
    }
}