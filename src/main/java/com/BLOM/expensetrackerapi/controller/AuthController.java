package com.BLOM.expensetrackerapi.controller;

import com.BLOM.expensetrackerapi.entity.AuthModel;
import com.BLOM.expensetrackerapi.entity.JwtResponse;
import com.BLOM.expensetrackerapi.entity.User;
import com.BLOM.expensetrackerapi.entity.UserModel;
import com.BLOM.expensetrackerapi.security.CustomUserDetailsService;
import com.BLOM.expensetrackerapi.service.BlackListService;
import com.BLOM.expensetrackerapi.service.UserService;
import com.BLOM.expensetrackerapi.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.BLOM.expensetrackerapi.repository.UserRepository;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BlackListService blackListService;

    @Autowired
    private UserRepository userRepository;



    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthModel authModel) throws Exception {

        // Check email and password
        authenticate(authModel.getEmail(), authModel.getPassword());

        // Find the user
        User user = userRepository.findByEmail(authModel.getEmail())
                .orElseThrow(() -> new Exception("User not found"));

        // Do not allow login before email verification
        if (!user.isVerified()) {throw new Exception("Please verify your email before signing in");}

        // Load user details
        final UserDetails userDetails =userDetailsService.loadUserByUsername(authModel.getEmail());
        // Generate JWT
        final String token = jwtTokenUtil.generateToken(userDetails);

        return new ResponseEntity<>(new JwtResponse(token),HttpStatus.OK);
    }




    // -----Authentication-----
    private void authenticate(String email, String password) throws Exception{

        try { // authentication manager asks spring if credentials are valid
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        } catch (DisabledException e) {
            throw new Exception("User disabled");
        }catch (BadCredentialsException e) {
            throw new Exception("Bad Credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> save(@Valid @RequestBody UserModel user) {
        return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/signout")
    public void logout(HttpServletRequest Request) {
        String jwtToken = extractJwtTokenFromRequest(Request);
        blackListService.addTokenToBlacklist(jwtToken);
    }

    private String extractJwtTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Bearer
        }
         return null;
    }
}
