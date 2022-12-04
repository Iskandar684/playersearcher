package ru.iskandar.playersearcher.controller;

import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.extern.java.Log;

/**
 * Отправитель электронного письма.
 */
@Log
@Service
public class EmailService {

    @Autowired
    private JavaMailSender _emailSender;

    public void sendEmail(@NonNull String aToAddress, @NonNull String aSubject,
            @NonNull String aMessage) {

        try {
            _emailSender.send(createSimpleMailMessage(aToAddress, aSubject, aMessage));
        } catch (Exception e) {
            log.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private SimpleMailMessage createSimpleMailMessage(@NonNull String aToAddress,
            @NonNull String aSubject,
            @NonNull String aMessage) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(aToAddress);
        simpleMailMessage.setSubject(aSubject);
        simpleMailMessage.setText(aMessage);
        return simpleMailMessage;
    }

}
