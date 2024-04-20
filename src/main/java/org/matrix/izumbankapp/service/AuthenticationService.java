package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.auth.*;

import java.security.Principal;

public interface AuthenticationService {
    AuthenticationResponseDto register(RegisterRequest request);

    AuthenticationResponseDto authenticate(AuthenticationRequest request);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);


}
