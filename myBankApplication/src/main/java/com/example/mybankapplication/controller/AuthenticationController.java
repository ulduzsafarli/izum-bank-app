package com.example.mybankapplication.controller;

import com.example.mybankapplication.model.auth.AuthenticationRequest;
import com.example.mybankapplication.model.auth.AuthenticationResponseDto;
import com.example.mybankapplication.model.auth.ChangePasswordRequest;
import com.example.mybankapplication.model.auth.RegisterRequest;
import com.example.mybankapplication.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    //log out

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }


//    @PutMapping("/user/{userEmail}/change-password/{newPassword}") //for user
//    public ResponseEntity<Void> authenticate(
//            @PathVariable @Email String userEmail,
//            @PathVariable @NotBlank(message = "Password is required")
//            @Size(min = 4, message = "Password must be at least 4 characters long")
//            String newPassword
//    ) {
//        service.changePassword(userEmail, newPassword);
//        return new ResponseEntity<>(HttpStatus.OK);    }


//    @DeleteMapping("/admin/delete/{email}")
//    public void delete(@PathVariable @Email String email){
//        service.deleteUser(email);
//    }



}