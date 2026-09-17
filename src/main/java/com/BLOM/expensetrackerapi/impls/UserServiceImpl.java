package com.BLOM.expensetrackerapi.impls;

import com.BLOM.expensetrackerapi.entity.Role;
import com.BLOM.expensetrackerapi.entity.User;
import com.BLOM.expensetrackerapi.entity.UserModel;
import com.BLOM.expensetrackerapi.exceptions.ResourceNotFoundException;
import com.BLOM.expensetrackerapi.exceptions.ItemExistsException;
import com.BLOM.expensetrackerapi.repository.UserRepository;
import com.BLOM.expensetrackerapi.service.EmailService;
import com.BLOM.expensetrackerapi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;


@Service
public
class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @Override
    public User createUser(UserModel user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ItemExistsException(
                    "User is already registered with email: " + user.getEmail()
            );
        }

        User newUser = new User();

        BeanUtils.copyProperties(user, newUser); //copy the registration information

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Role.USER);

        // Generate a random 6-digit verification code
        String verificationCode = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        newUser.setVerified(false);
        newUser.setVerificationCode(verificationCode);

        // Code expires after 10 minutes
        newUser.setVerificationCodeExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)));

        User savedUser = userRepository.save(newUser);

        // Send the code to the user's email
        emailService.sendVerificationCode(savedUser.getEmail(), verificationCode);
        return savedUser;
    }




    @Override
    public User readUser() {
        Long userId = getLoggedInUser().getId();
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for the id: "+ userId));
        // gets from DB
    }

    @Override
    public User updateUser(UserModel user) {
        User existingUser = readUser();
        existingUser.setName(user.getName() !=null ? user.getName() : existingUser.getName());
        existingUser.setEmail(user.getEmail() !=null ? user.getEmail() : existingUser.getEmail());
        existingUser.setPassword(user.getPassword() !=null ? passwordEncoder.encode(user.getPassword()): existingUser.getPassword());
        existingUser.setAge(user.getAge() !=null ? user.getAge() : existingUser.getAge());
        return userRepository.save(existingUser);

    }

    @Override
    public void deleteUser() {
        User existingUser = readUser();
        userRepository.delete(existingUser);
    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found for the email: " + email));
    }


    @Override
    public boolean verifyEmail(String email, String code) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found for email: " + email
                        )
                );

        if (user.isVerified()) return true;

        if (user.getVerificationCodeExpiresAt() == null ||
                user.getVerificationCodeExpiresAt()
                        .before(Timestamp.valueOf(LocalDateTime.now()))) {

            throw new RuntimeException("Verification code has expired");
        }

        if (!user.getVerificationCode().equals(code)) return false;

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
        return true;
    }



    @Override
    public void resendVerificationCode(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) {throw new IllegalArgumentException("Email is already verified"); }

        String verificationCode = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        user.setVerificationCode(verificationCode);

        user.setVerificationCodeExpiresAt(new Timestamp(System.currentTimeMillis() + (10 * 60 *1000))); // 10 minutes

        userRepository.save(user);
        emailService.sendVerificationCode( user.getEmail(),verificationCode);
    }


}
