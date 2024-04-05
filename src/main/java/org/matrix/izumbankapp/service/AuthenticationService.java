package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.auth.*;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface AuthenticationService {
    AuthenticationResponseDto register(RegisterRequest request);

    AuthenticationResponseDto authenticate(AuthenticationRequest request);

    ResponseDto changePassword(ChangePasswordRequest request, Principal connectedUser);


}
