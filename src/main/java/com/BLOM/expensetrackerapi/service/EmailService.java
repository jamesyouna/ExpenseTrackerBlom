package com.BLOM.expensetrackerapi.service;

public interface EmailService {

    void sendVerificationCode(String email, String code);
}
