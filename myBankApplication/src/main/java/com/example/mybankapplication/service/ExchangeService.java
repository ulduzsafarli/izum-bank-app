package com.example.mybankapplication.service;

import com.example.mybankapplication.model.auth.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ExchangeService {

    ResponseDto fetchCurrenciesAndSave();
}