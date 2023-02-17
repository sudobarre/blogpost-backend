package com.fede.blog.controller;
import com.fede.blog.dto.*;

import com.fede.blog.dto.response.PostResponse;
import com.fede.blog.service.AuthService;
import com.fede.blog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MODERATOR')")
    @PutMapping("/edit")
    public ResponseEntity<?> editUser(UserDto userDto){
        userService.editUser(userDto);
        return new ResponseEntity<>(OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/ban/{username}")
    public ResponseEntity<?> banUser(@PathVariable(name = "username") String username){
        userService.banUser(username);
        return new ResponseEntity<>(OK);
    }


}
