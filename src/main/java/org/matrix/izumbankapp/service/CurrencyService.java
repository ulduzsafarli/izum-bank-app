package org.matrix.izumbankapp.service;

import org.springframework.stereotype.Service;

@Service
public interface CurrencyService {
    void fetch();
    String getFile();

}
