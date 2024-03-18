package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.model.support.SupportDto;
import com.example.mybankapplication.model.support.SupportResponseDto;
import com.example.mybankapplication.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Objects;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final Environment environment;
    private static final String EMAIL_SUBJECT = "Izum Bank - Support Form";

    public void sendResponseEmail(String to, SupportResponseDto supportResponseDto) {
        try {
            MimeMessage mimeMessage = createMimeMessage(to, supportResponseDto.getResponseMessage());
            sendMimeMessage(mimeMessage);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException | MailException e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }

    public void sendSupportEmail(SupportDto supportDto) {
        try {
            MimeMessage mimeMessage = createSupportMimeMessage(supportDto);
            sendMimeMessage(mimeMessage);
            log.info("Support email sent successfully");
        } catch (MessagingException | MailException e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }

    private MimeMessage createMimeMessage(String to, String responseMessage) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        Context context = new Context();
        context.setVariable("responseMessage", responseMessage);

        String emailContent = templateEngine.process("responseEmail", context);
        helper.setTo(to);
        helper.setSubject(EMAIL_SUBJECT);
        helper.setText(emailContent, true);
        return mimeMessage;
    }

    private MimeMessage createSupportMimeMessage(SupportDto supportDto) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Context context = new Context();
        context.setVariable("supportDto", supportDto);

        String emailContent = templateEngine.process("supportEmail", context);

        helper.setTo(Objects.requireNonNull(environment.getProperty("spring.mail.to")));
        helper.setSubject(EMAIL_SUBJECT);
        helper.setText(emailContent, true);
        helper.setFrom(supportDto.getEmail());

        return mimeMessage;
    }

    private void sendMimeMessage(MimeMessage mimeMessage) {
        try {
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }
}
