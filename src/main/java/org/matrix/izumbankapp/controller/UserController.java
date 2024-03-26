package org.matrix.izumbankapp.controller;

import org.matrix.izumbankapp.model.accounts.AccountsUserResponse;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.model.users.UserAccountsResponse;
import org.matrix.izumbankapp.model.users.UserCreateDto;
import org.matrix.izumbankapp.model.users.UserUpdateDto;
import org.matrix.izumbankapp.model.users.UserResponse;
import org.matrix.izumbankapp.model.users.profile.UserProfileDto;
import org.matrix.izumbankapp.model.users.profile.UserProfileFilterDto;
import org.matrix.izumbankapp.service.AccountService;
import org.matrix.izumbankapp.service.UserService;
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
    private final AccountService accountService;

    @GetMapping("/search")
    public Page<UserProfileDto> getUsersByFilter(UserProfileFilterDto filter, Pageable pageable) {
        return userService.findUsersByFilter(filter, pageable);
    }
    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user details as a ResponseEntity
     */
    @GetMapping("/userId")
    public ResponseEntity<UserResponse> getUserById(@RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
    }
    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return the user details as a ResponseEntity
     */

    @GetMapping("/email")
    public ResponseEntity<UserResponse> getUserByEmail(@Valid @RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmail(email));
    }

    /**
     * Retrieves all users.
     *
     * @return The list of user DTOs.
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUser());
    }

    /**
     * Creates a new user.
     *
     * @param user the user data transfer object.
     * @return the response entity containing the response.
     */
    @PostMapping
    public ResponseEntity<ResponseDto> createUser(@Valid @RequestBody UserCreateDto user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    /**
     * Updates user's information.
     *
     * @param id The ID of the user to update.
     * @param user The user to update.
     * @return The response entity containing the response.
     */
    @PutMapping
    public ResponseEntity<ResponseDto> updateUser(@RequestParam Long id, @Valid @RequestBody UserUpdateDto user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user));
    }

    /**
     * Deletes the user with the ID.
     *
     * @param id The ID of the user to delete.
     * @return the response entity containing the response.
     */
    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteUserById(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUserById(id));
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<UserAccountsResponse> readUserByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getUserByAccountNumber(accountNumber));
    }

    @GetMapping("{userId}/accounts")
    public ResponseEntity<List<AccountsUserResponse>> getAllAccountsByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAllAccountsByUserId(userId));
    }

}
