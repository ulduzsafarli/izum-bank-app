package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.support.EmailAnswerDto;
import org.matrix.izumbankapp.model.support.SupportDto;
import org.springframework.stereotype.Service;

@Service
public interface EmailSendingService {
    void sendResponseEmail(String to, EmailAnswerDto emailAnswerDto);

    void sendNotificationEmail(String to, EmailAnswerDto emailAnswerDto);

    void sendSupportEmail(SupportDto supportDto);


}
