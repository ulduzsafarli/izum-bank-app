package com.example.mybankapplication.controller;

import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.model.users.UserRequestDto;
import com.example.mybankapplication.model.users.profile.UserProfileFilterDto;
import com.example.mybankapplication.model.users.UserResponse;
import com.example.mybankapplication.model.users.UserRequest;
import com.example.mybankapplication.model.users.profile.UserProfileDto;
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
    public Page<UserProfileDto> getUsersByFilter(@Valid UserProfileFilterDto userProfileFilterDto, Pageable pageable) {
        return userService.findUsersByFilter(userProfileFilterDto, pageable);
    }
    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user details as a ResponseEntity
     */
    @GetMapping("/userId/{userId}")
    public ResponseEntity<UserResponse> readUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.readUserById(userId));
    }
    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return the user details as a ResponseEntity
     */

    @GetMapping("email/{email}")
    public ResponseEntity<UserResponse> readUserByEmail(@Valid @PathVariable String email) {
        return ResponseEntity.ok(userService.readUserByEmail(email));
    }

    /**
     * Retrieves all users.
     *
     * @return The list of user DTOs.
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    /**
     * Creates a new user.
     *
     * @param user the user data transfer object.
     * @return the response entity containing the response.
     */
    @PostMapping
    public ResponseEntity<ResponseDto> addUser(@Valid @RequestBody UserRequestDto user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    /**
     * Updates user's information.
     *
     * @param id The ID of the user to update.
     * @param user The user to update.
     * @return The response entity containing the response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto user) {
        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);
    }

    /**
     * Deletes the user with the ID.
     *
     * @param id The ID of the user to delete.
     * @return the response entity containing the response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUserById(id));
    }


//    @PatchMapping("/{id}")
//    public ResponseEntity<ResponseDto> updateUserStatus(@PathVariable Long id, @RequestBody UserUpdateStatus userUpdate) {
//        return new ResponseEntity<>(userService.updateUserStatus(id, userUpdate), HttpStatus.OK);
//    }

    /**
     * Retrieves the user with the specified account ID.
     *
     * @param accountId The account ID of the user to retrieve.
     * @return The user DTO associated with the account ID.
     */
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<UserResponse> readUserByAccountNumber(@PathVariable Long accountId) {
        return ResponseEntity.ok(userService.readUserByAccountId(accountId));
    }
}
