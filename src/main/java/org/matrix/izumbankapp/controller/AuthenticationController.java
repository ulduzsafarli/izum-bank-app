package org.matrix.izumbankapp.controller;

import org.matrix.izumbankapp.model.auth.*;
import org.matrix.izumbankapp.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    //log out

    @PatchMapping("/change-password")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody ChangePasswordRequest request,
                                                      Principal connectedUser) {
        return ResponseEntity.ok(authenticationService.changePassword(request, connectedUser));
    }


}