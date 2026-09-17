package com.BLOM.expensetrackerapi.controller;

import com.BLOM.expensetrackerapi.entity.User;
import com.BLOM.expensetrackerapi.entity.UserModel;
import com.BLOM.expensetrackerapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> readUser() {

        return new ResponseEntity<User>(userService.readUser(), HttpStatus.OK);
    }


    @PutMapping("/profile")
    public ResponseEntity<User> updateUser(@RequestBody UserModel user) {
        return new ResponseEntity<User>(userService.updateUser(user), HttpStatus.OK);

    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<HttpStatus> deleteUser() {
        userService.deleteUser();
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String code) {

        boolean verified = userService.verifyEmail(email, code);
        if (verified)  return ResponseEntity.ok("Email verified successfully");

        return ResponseEntity.badRequest().body("Invalid verification code");
    }


    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationCode(
            @RequestParam String email) {

        userService.resendVerificationCode(email);

        return ResponseEntity.ok("Verification code resent successfully");
    }
}
