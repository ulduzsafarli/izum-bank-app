package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.exception.supports.EmailSendingException;
import org.matrix.izumbankapp.model.support.SupportDto;
import org.matrix.izumbankapp.model.support.EmailAnswerDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.service.EmailSendingService;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Objects;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSendingServiceImpl implements EmailSendingService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final Environment environment;
    private static final String IZUM_BANK_SUPPORT_FORM = "Izum Bank - Support Form";
    private static final String IZUM_BANK_NOTIFICATION_FORM = "Izum Bank - Notification Form";
    
    public void sendResponseEmail(String to, EmailAnswerDto emailAnswerDto) {
        try {
            MimeMessage mimeMessage = createMimeMessage(to, emailAnswerDto.getResponseMessage(), IZUM_BANK_SUPPORT_FORM);
            sendMimeMessage(mimeMessage);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException | MailException e) {
            throw new EmailSendingException("Error while sending response email", e);
        }
    }
    public void sendNotificationEmail(String to, EmailAnswerDto emailAnswerDto) {
        try {
            MimeMessage mimeMessage = createMimeMessage(to, emailAnswerDto.getResponseMessage(), IZUM_BANK_NOTIFICATION_FORM);
            sendMimeMessage(mimeMessage);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException | MailException e) {
            throw new EmailSendingException("Error while sending notification email", e);
        }
    }

    public void sendSupportEmail(SupportDto supportDto) {
        try {
            MimeMessage mimeMessage = createSupportMimeMessage(supportDto);
            sendMimeMessage(mimeMessage);
            log.info("Support email sent successfully");
        } catch (MessagingException | MailException e) {
            throw new EmailSendingException("Error while sending support email", e);
        }
    }

    private MimeMessage createMimeMessage(String to, String responseMessage, String subject) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        Context context = new Context();
        context.setVariable("responseMessage", responseMessage);

        String emailContent = templateEngine.process("responseEmail", context);
        helper.setTo(to);
        helper.setSubject(subject);
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
        helper.setSubject(IZUM_BANK_SUPPORT_FORM);
        helper.setText(emailContent, true);
        helper.setFrom(supportDto.getEmail());

        return mimeMessage;
    }

    private void sendMimeMessage(MimeMessage mimeMessage) {
        try {
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new EmailSendingException("Error while sending email", e);
        }
    }
}
