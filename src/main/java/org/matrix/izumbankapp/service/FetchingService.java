package org.matrix.izumbankapp.service;

import org.springframework.stereotype.Service;

@Service
public interface FetchingService {
    void fetch();
    String getFile();

}
