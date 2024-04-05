package org.matrix.izumbankapp.controller;

import org.matrix.izumbankapp.model.users.UserAccountsResponse;
import org.matrix.izumbankapp.model.users.UserCreateDto;
import org.matrix.izumbankapp.model.users.UserUpdateDto;
import org.matrix.izumbankapp.model.users.UserResponse;
import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
import org.matrix.izumbankapp.model.users.profile.UserProfileFilterDto;
import org.matrix.izumbankapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public Page<UserProfileDto> getByFilter(UserProfileFilterDto filter, Pageable pageable) {
        return userService.findByFilter(filter, pageable);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getByEmail(@Valid @PathVariable String email) {
        return userService.getByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserCreateDto user) {
        return userService.create(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse update(@RequestParam Long id, @Valid @RequestBody UserUpdateDto user) {
        return userService.update(id, user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long id) {
        userService.delete(id);
    }

    @GetMapping("/account/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public UserAccountsResponse getByAccountNumber(@PathVariable String accountNumber) {
        return userService.getByAccountNumber(accountNumber);
    }

}
