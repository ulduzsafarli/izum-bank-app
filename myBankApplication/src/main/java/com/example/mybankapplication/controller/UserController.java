package com.example.mybankapplication.controller;

import com.example.mybankapplication.model.users.UserFilterDto;
import com.example.mybankapplication.model.users.UserResponse;
import com.example.mybankapplication.model.users.UserRequest;
import com.example.mybankapplication.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public Page<UserResponse> getUsersByFilter(@Valid UserFilterDto userFilterDto, Pageable pageable) {
        return userService.findUsersByFilter(userFilterDto, pageable);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@Valid @PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/search/phoneNumber/{phoneNumber}")
    public ResponseEntity<UserResponse> getUserByPhoneNumber(@Valid @PathVariable String phoneNumber) {
        return ResponseEntity.ok(userService.getUserByPhoneNumber(phoneNumber));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PostMapping
    public ResponseEntity<Void> addUser(@Valid @RequestBody UserRequest user) {
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest user) {
        userService.updateUser(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
