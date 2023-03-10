package com.fede.blog.controller;


import com.fede.blog.dto.request.LoginRequest;
import com.fede.blog.dto.request.SignupRequest;
import com.fede.blog.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(value="/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletRequest request) {
        return authService.signup(signupRequest, request);

    }

    @GetMapping(value="accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable(name = "token") String token) {
        authService.verifyAccount(token);
        //too lazy to create a nice template for the account activation
        return new ResponseEntity<>("Account activated successfully! You can now close this window and log in with your registered username.", OK);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping(value="/refreshtoken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping(value="/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout() {
        return authService.logout();
    }

    /*
    //TODO
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody String email){
        authService.forgotPassword(email);
        return new ResponseEntity<>("Please check your email inbox to change your password.",
                OK);
    }

     */
}