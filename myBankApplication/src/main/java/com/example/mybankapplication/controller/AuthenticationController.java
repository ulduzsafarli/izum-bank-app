package com.example.mybankapplication.controller;


import com.example.mybankapplication.model.auth.AuthenticationRequest;
import com.example.mybankapplication.model.auth.AuthenticationResponseDto;
import com.example.mybankapplication.model.auth.RegisterRequest;
import com.example.mybankapplication.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @DeleteMapping("/admin/delete/{email}")
//    public void delete(@PathVariable @Email String email){
//        service.deleteUser(email);
//    }


    //log out
}