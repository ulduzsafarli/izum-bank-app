package com.example.mybankapplication.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

    private String title;
    private int status;
    private String detail;
    private Date timeStamp;
    private String path;
    private String developerMessage;
    private Map<String, List<ValidationError>> errors = new HashMap<>();

    public void addError(String fieldName, String errorMessage) {
        // Получаем список ошибок для данного поля или создаем новый список, если его еще нет
        List<ValidationError> errorList = errors.computeIfAbsent(fieldName, k -> new ArrayList<>());
        // Создаем объект ValidationError и добавляем его в список ошибок для данного поля
        ValidationError validationError = new ValidationError();
        validationError.setMessage(errorMessage);
        errorList.add(validationError);
    }

}
