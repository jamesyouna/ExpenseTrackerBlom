package com.BLOM.expensetrackerapi.service;

import com.BLOM.expensetrackerapi.entity.User;
import com.BLOM.expensetrackerapi.entity.UserModel;

public interface UserService {
    User createUser(UserModel user);

    User readUser();

    User updateUser(UserModel user);

    void deleteUser();

    User getLoggedInUser();

    boolean verifyEmail(String email, String code);

    void resendVerificationCode(String email);
}
